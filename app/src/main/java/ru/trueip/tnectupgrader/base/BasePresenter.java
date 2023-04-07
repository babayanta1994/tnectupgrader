package ru.trueip.tnectupgrader.base;

import java.lang.ref.WeakReference;

import ru.trueip.tnectupgrader.app.App;

/**
 * Created by user on 07-Sep-17.
 */

public class BasePresenter<C extends BaseContract> {

    private static final String TAG = BasePresenter.class.getSimpleName();


    public BasePresenter() {
        App.getMainComponent().inject((BasePresenter<BaseContract>) this);
    }

    private WeakReference<C> contractReference = null;

    public void attachToView(C contract) {
        contractReference = new WeakReference<>(contract);
    }

    public void detachView() {
        if (contractReference != null)
            contractReference.clear();
        contractReference = null;
    }

    protected C getContract() {
        if (contractReference == null) {
            return null;
        }
        return contractReference.get();
    }



}
