#!/usr/bin/env python3
"""Read the first 15 seconds of supplied audio; emit 8 kHz mono IMA ADPCM.
Uses NumPy and system libsndfile. The original recording is never modified.
"""
import argparse
import ctypes
import os
from pathlib import Path
import wave
import numpy as np

RATE = 8000
SECONDS = 15
STEPS = (7,8,9,10,11,12,13,14,16,17,19,21,23,25,28,31,34,37,41,45,50,55,
         60,66,73,80,88,97,107,118,130,143,157,173,190,209,230,253,279,307,
         337,371,408,449,494,544,598,658,724,796,876,963,1060,1166,1282,
         1411,1552,1707,1878,2066,2272,2499,2749,3024,3327,3660,4026,
         4428,4871,5358,5894,6484,7132,7845,8630,9493,10442,11487,12635,
         13899,15289,16818,18500,20350,22385,24623,27086,29794,32767)
INDEX = (-1,-1,-1,-1,2,4,6,8)

class SFInfo(ctypes.Structure):
    _fields_ = [('frames',ctypes.c_int64),('samplerate',ctypes.c_int),
                ('channels',ctypes.c_int),('format',ctypes.c_int),
                ('sections',ctypes.c_int),('seekable',ctypes.c_int)]


def load_excerpt(path):
    lib = ctypes.CDLL('libsndfile.so.1')
    lib.sf_open.argtypes = [ctypes.c_char_p,ctypes.c_int,ctypes.POINTER(SFInfo)]
    lib.sf_open.restype = ctypes.c_void_p
    lib.sf_readf_float.argtypes = [ctypes.c_void_p,ctypes.POINTER(ctypes.c_float),ctypes.c_int64]
    lib.sf_readf_float.restype = ctypes.c_int64
    lib.sf_close.argtypes = [ctypes.c_void_p]
    info = SFInfo()
    handle = lib.sf_open(os.fsencode(path), 0x10, ctypes.byref(info))
    if not handle:
        raise RuntimeError('libsndfile cannot decode the supplied recording')
    try:
        buf = np.empty((SECONDS * info.samplerate, info.channels), dtype=np.float32)
        frames = lib.sf_readf_float(handle, buf.ctypes.data_as(ctypes.POINTER(ctypes.c_float)), len(buf))
    finally:
        lib.sf_close(handle)
    mono = buf[:frames].mean(axis=1).astype(np.float64)
    # Low-pass before downsampling: avoids folding the original high frequencies.
    taps = np.arange(401) - 200
    cutoff = 3200.0 / info.samplerate
    kernel = 2 * cutoff * np.sinc(2 * cutoff * taps) * np.blackman(len(taps))
    kernel /= kernel.sum()
    filtered = np.convolve(mono, kernel, mode='same')
    positions = np.arange(RATE * SECONDS) * info.samplerate / RATE
    reduced = np.interp(positions, np.arange(len(filtered)), filtered, right=0.0)
    reduced -= reduced.mean()
    peak = max(float(np.max(np.abs(reduced))), 1e-9)
    reduced *= 0.70 / peak
    # Short fades avoid switching clicks; retain precisely the first 15 seconds.
    fade = int(RATE * 0.02)
    reduced[:fade] *= np.linspace(0, 1, fade)
    reduced[-fade:] *= np.linspace(1, 0, fade)
    return np.rint(np.clip(reduced, -1, 1) * 32767).astype('<i2')


def encode(samples):
    predictor = index = 0
    nibbles = []
    decoded = []
    for target in samples:
        step = STEPS[index]
        diff = int(target) - predictor
        code = 8 if diff < 0 else 0
        diff = abs(diff)
        delta = step >> 3
        if diff >= step:
            code |= 4; diff -= step; delta += step
        if diff >= step >> 1:
            code |= 2; diff -= step >> 1; delta += step >> 1
        if diff >= step >> 2:
            code |= 1; delta += step >> 2
        predictor += -delta if code & 8 else delta
        predictor = min(32767, max(-32768, predictor))
        index = min(88, max(0, index + INDEX[code & 7]))
        nibbles.append(code)
        decoded.append(predictor)
    packed = bytes(nibbles[i] | ((nibbles[i+1] if i+1 < len(nibbles) else 0) << 4)
                   for i in range(0, len(nibbles), 2))
    return packed, np.array(decoded, dtype='<i2')


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('input', type=Path)
    parser.add_argument('--preview', type=Path)
    args = parser.parse_args()
    project = Path(__file__).resolve().parents[1]
    samples = load_excerpt(args.input)
    packed, decoded = encode(samples)
    header = '''/* First 15 seconds of the user-supplied recording. Mono IMA ADPCM. */
#ifndef AUDIO_CLIP_H
#define AUDIO_CLIP_H
#include <stdint.h>
#define AUDIO_SAMPLE_RATE 8000u
#define AUDIO_SAMPLE_COUNT 120000u
#define AUDIO_CLIP_BYTES 60000u
extern const uint8_t audio_clip[AUDIO_CLIP_BYTES];
#endif
'''
    (project / 'Inc/audio_clip.h').write_text(header)
    rows = ['  ' + ', '.join(f'0x{x:02x}' for x in packed[i:i+16]) + ','
            for i in range(0, len(packed), 16)]
    (project / 'Src/audio_clip.c').write_text('#include "audio_clip.h"\n\nconst uint8_t audio_clip[AUDIO_CLIP_BYTES] = {\n' + '\n'.join(rows) + '\n};\n')
    if args.preview:
        args.preview.parent.mkdir(parents=True, exist_ok=True)
        with wave.open(str(args.preview), 'wb') as out:
            out.setnchannels(1); out.setsampwidth(2); out.setframerate(RATE)
            out.writeframes(decoded.tobytes())
    error = samples.astype(np.float64) - decoded.astype(np.float64)
    snr = 10 * np.log10(np.mean(samples.astype(np.float64)**2) / max(np.mean(error**2), 1e-9))
    print(f'{len(samples)/RATE:.2f} s, {RATE} Hz mono, 4 bits/sample, {len(packed)} bytes, ADPCM SNR {snr:.1f} dB')

if __name__ == '__main__':
    main()
