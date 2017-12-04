package com.nodream.xskj.my

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.nodream.xskj.commonlib.base.BaseActivity

@Route(path = "/my/me")
class MyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_me)
    }
}
