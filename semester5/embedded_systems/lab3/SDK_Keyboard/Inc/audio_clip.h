/* First 15 seconds of the user-supplied recording. Mono IMA ADPCM. */
#ifndef AUDIO_CLIP_H
#define AUDIO_CLIP_H
#include <stdint.h>
#define AUDIO_SAMPLE_RATE 8000u
#define AUDIO_SAMPLE_COUNT 120000u
#define AUDIO_CLIP_BYTES 60000u
extern const uint8_t audio_clip[AUDIO_CLIP_BYTES];
#endif
