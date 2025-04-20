package com.lab6.common.managers;

import com.lab6.common.models.MusicBand;

import java.util.Stack;

public interface ICollectionManager {
    MusicBand getById(Long id);
    Long getFreeId();
    boolean add(MusicBand band);
    Stack<MusicBand> getCollection();
    void removeById(Long id);
}