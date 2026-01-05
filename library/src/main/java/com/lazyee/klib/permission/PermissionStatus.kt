package com.lazyee.klib.permission

class PermissionStatus(val permission: String) {
    var isGranted: Boolean = false
    var doNotAskAgain: Boolean = false
}