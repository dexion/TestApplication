package com.dexion.testapplication.fragments.ShowShiftType;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dexion.testapplication.R;
import com.dexion.testapplication.models.ShiftType;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class ShiftTypeNameFragment extends Fragment {

    private static final String ARG_PARAM1 = "_id";

    private Button btn_change_color;
    private int currentBackgroundColor = 0xffff0000;

    private long mId;

    private ShiftType st;

    public ShiftTypeNameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getLong(ARG_PARAM1);
            st = ShiftType.getById(mId);
            currentBackgroundColor = st.color;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shift_type_name, container, false);

        btn_change_color = (Button) v.findViewById(R.id.change_shift_type_color);
        btn_change_color.setBackgroundColor(currentBackgroundColor);
        btn_change_color.setOnClickListener(new ColorButtonClickListener());

        EditText et = (EditText) v.findViewById(R.id.shift_type_name);
        et.setText(st.name);

        return v;
    }


    private class ColorButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            ColorPickerDialogBuilder
                    .with(getActivity())
                    .setTitle(getString(R.string.choose_color))
                    .initialColor(currentBackgroundColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                        }
                    })
                    .setPositiveButton("Ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            st.color = selectedColor;
                            st.save();
                            btn_change_color.setBackgroundColor(selectedColor);
                        }
                    })
                    .setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build()
                    .show();
        }
    }
}
