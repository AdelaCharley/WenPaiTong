package com.qunshang.wenpaitong.equnshang.fragment;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public abstract class BaseShareFragment extends Fragment {

    public boolean isQrCodeInited = false;

    public abstract Bitmap getRootBitmap();

}
