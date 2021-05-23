package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;


/***
 * Created by Amirreza Erfanian on 2018/July/26.
 * v : 1.0.0
 */

public class GeneralDialog {

    private Runnable bodyRunnable = null;
    private Runnable dismissBody = null;
    private ButtonModel firstBtn = null;
    private ButtonModel secondBtn = null;
    private DismissListener listener;
    private Listener descListener;
    private String messageText = "";
    private int visibility;
    private boolean cancelable = true;
    private boolean singleInstance = false;

    public static int TYPE = 0;
    public static final int WARNING = 1;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;
    public static final int INFO = 4;

    private class ButtonModel {
        String text;
        Runnable body;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Runnable getBody() {
            return body;
        }

        public void setBody(Runnable body) {
            this.body = body;
        }
    }

    interface DismissListener {
        void onDismiss();
    }

    public interface Listener {
        void onDescription(String message);
    }

    public ir.taxi1880.manager.dialog.GeneralDialog isSingleMode(boolean singleInstance) {
        this.singleInstance = singleInstance;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog onDescriptionListener(Listener listener) {
        this.descListener = listener;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog messageVisibility(int visible) {
        this.visibility = visible;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog onDismissListener(DismissListener listener) {
        this.listener = listener;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog afterDismiss(Runnable dismissBody) {
        this.dismissBody = dismissBody;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog firstButton(String name, Runnable body) {
        firstBtn = new ButtonModel();
        firstBtn.setBody(body);
        firstBtn.setText(name);
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog secondButton(String name, Runnable body) {
        secondBtn = new ButtonModel();
        secondBtn.setBody(body);
        secondBtn.setText(name);
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog bodyRunnable(Runnable bodyRunnable) {
        this.bodyRunnable = bodyRunnable;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog message(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public GeneralDialog type(int type) {
        this.TYPE = type;
        return this;
    }

    public ir.taxi1880.manager.dialog.GeneralDialog cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    Unbinder unbinder;

    @BindView(R.id.txtMessage)
    TextView txtMessage;

    @BindView(R.id.llBtnView)
    LinearLayout llBtnView;

    @BindView(R.id.btnFirst)
    Button btnFirst;

    @BindView(R.id.btnSecond)
    Button btnSecond;

    @BindView(R.id.imgType)
    ImageView imgType;

    @OnClick(R.id.btnFirst)
    void onFirstPress() {
        dismiss();
        if (firstBtn != null) {
            if (firstBtn.getBody() != null) {
                firstBtn.getBody().run();
            }
        }
    }

    @OnClick(R.id.btnSecond)
    void onSecondPress() {
        dismiss();

        if (secondBtn != null) {
            if (secondBtn.getBody() != null)
                secondBtn.getBody().run();
        }
    }

    @BindView(R.id.divider_st)
    View divider_st;

    private Dialog dialog;
    private static Dialog staticDialog = null;

    public void show() {
        if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
            return;
        Dialog tempDialog = null;
        if (singleInstance) {
            if (staticDialog != null) {
                staticDialog.dismiss();
                staticDialog = null;
            }
            staticDialog = new Dialog(MyApplication.currentActivity);
            tempDialog = staticDialog;
        } else {
            dialog = new Dialog(MyApplication.currentActivity);
            tempDialog = dialog;
        }
        tempDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        tempDialog.setContentView(R.layout.dialog_general);
        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = tempDialog.getWindow().getAttributes();
        tempDialog.getWindow().setAttributes(wlp);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        tempDialog.setCancelable(cancelable);
        unbinder = ButterKnife.bind(this, tempDialog);
        TypefaceUtil.overrideFonts(tempDialog.getWindow().getDecorView());

        txtMessage.setText(messageText);
        if (messageText.isEmpty()) {
            txtMessage.setVisibility(View.GONE);
        }
        if (firstBtn == null) {
            btnFirst.setVisibility(View.GONE);
        } else {
            btnFirst.setText(firstBtn.getText());
        }
        if (secondBtn == null) {
            btnSecond.setVisibility(View.GONE);
        } else {
            btnSecond.setText(secondBtn.getText());
        }
        if (secondBtn == null) {
            divider_st.setVisibility(View.GONE);
        }
        if (firstBtn == null && secondBtn == null) {
            llBtnView.setVisibility(View.GONE);
        }
        if (bodyRunnable != null)
            bodyRunnable.run();

        tempDialog.setOnDismissListener(dialog -> {
            if (dismissBody != null)
                dismissBody.run();
        });
        tempDialog.show();

        switch (TYPE) {
            case 0: //nothing
                imgType.setVisibility(View.GONE);
                break;

            case 1: //warning
                imgType.setImageResource(R.drawable.ic_warning);
                break;

            case 2: //success
                imgType.setImageResource(R.drawable.ic_success);
                break;

            case 3: //error
                imgType.setImageResource(R.drawable.ic_error);
                break;

            case 4: //info
                imgType.setImageResource(R.drawable.ic_info);
                break;

        }
    }

    // dismiss center control
    public void dismiss() {
        try {
            if (listener != null) {
                listener.onDismiss();
            }
            if (singleInstance) {
                if (staticDialog != null) {
                    staticDialog.dismiss();
                    staticDialog = null;
                }
            } else {
                if (dialog != null)
                    if (dialog.isShowing())
                        dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog = null;
    }
}
