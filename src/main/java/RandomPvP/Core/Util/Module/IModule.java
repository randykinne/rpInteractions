package RandomPvP.Core.Util.Module;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class IModule {

    String name;
    Object data;
    Object[] dataArray;

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) { this.data = data; }

    public void setDataArray(Object[] dataArray) { this.dataArray = dataArray; }

    public Object[] getDataArray() {
        return dataArray;
    }

    public IModule(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public IModule(String name, Object[] data) {
        this.name = name;
        this.dataArray = data;
    }
}
