package com.example.jinny.mymusic.Network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "tracklist",strict = false)
public class LocationResponse {
    @Element(name = "track")
    public TrackXML trackXML;
} 