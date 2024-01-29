package org.dieschnittstelle.mobile.android.skeleton.viewmodels;
import androidx.lifecycle.ViewModel;
public class ViewModelBase extends ViewModel
{
    private boolean Inited;
    public boolean isInited() { return Inited; }
    public void Init() { Inited = true; }
}
