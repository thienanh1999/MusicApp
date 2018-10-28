package com.example.jinny.mymusic.Network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "track",strict = false)
public class TrackXML {
    @Element(name = "location")
    public String location;
} 