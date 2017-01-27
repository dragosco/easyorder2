package motacojo.mbds.fr.easyorder30.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import motacojo.mbds.fr.easyorder30.R;

public class PreparingOrdersFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.preparing_orders_layout, container, false);
        return view;
    }
}
