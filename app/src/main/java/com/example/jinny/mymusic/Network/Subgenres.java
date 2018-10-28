package com.example.jinny.mymusic.Network;

import java.util.ArrayList;
import java.util.List;

public class Subgenres {
    public List<MusicTypeJSON> subgenres = new ArrayList<>();

    public Subgenres(List<MusicTypeJSON> musicTypeList) {
        this.subgenres = musicTypeList;
    }

    public class MusicTypeJSON {
        public String id;
        public String translation_key;

        @Override
        public String toString() {
            return "MysicTypeJSON{" +
                    "id='" + id + '\'' +
                    ", translattion_key='" + translation_key + '\'' +
                    '}';
        }
    }
}