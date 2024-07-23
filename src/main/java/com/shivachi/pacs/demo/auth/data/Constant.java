package com.shivachi.pacs.demo.auth.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Constant {
    ACTIVE("Active"),
    DELETED("Deleted"),
    DISABLED("Disabled");

    private final String constant;
}