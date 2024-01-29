package org.dieschnittstelle.mobile.android.skeleton.pages;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import org.dieschnittstelle.mobile.android.skeleton.viewmodels.ViewModelBase;
public abstract class ActivityBase<TViewModel extends ViewModelBase> extends AppCompatActivity
{
    private TViewModel ViewModel;
    protected TViewModel getViewModel() {return ViewModel;}

    protected abstract Class<TViewModel> getViewModelClass();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ViewModel = new ViewModelProvider(this).get(getViewModelClass());
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (!ViewModel.isInited())
        {
            // TODO offline-botschaft hier.
            onViewModelInit(ViewModel);
            ViewModel.Init();
        }
    }

    protected void onViewModelInit(TViewModel viewModel)
    {

    }
}
