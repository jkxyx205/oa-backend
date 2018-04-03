package com.yodean.oa.sys.workspace.dto;

import com.yodean.oa.sys.workspace.entity.Workspace;

/**
 * Created by rick on 4/3/18.
 * 参与人
 */
public class Participant extends Workspace {

    private String chineseName;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
}
