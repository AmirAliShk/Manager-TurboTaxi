package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.DialogGeneralBinding;
import ir.taxi1880.manager.helper.TypefaceUtil;

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
    DialogGeneralBinding binding;

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
        binding = DialogGeneralBinding.inflate(LayoutInflater.from(MyApplication.context));
        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tempDialog.setContentView(binding.getRoot());
        WindowManager.LayoutParams wlp = tempDialog.getWindow().getAttributes();
        tempDialog.getWindow().setAttributes(wlp);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        tempDialog.setCancelable(cancelable);
        TypefaceUtil.overrideFonts(binding.getRoot());

        binding.txtMessage.setText(messageText);
        if (messageText.isEmpty()) {
            binding.txtMessage.setVisibility(View.GONE);
        }
        if (firstBtn == null) {
            binding.btnFirst.setVisibility(View.GONE);
        } else {
            binding.btnFirst.setText(firstBtn.getText());
        }
        if (secondBtn == null) {
            binding.btnSecond.setVisibility(View.GONE);
        } else {
            binding.btnSecond.setText(secondBtn.getText());
        }
        if (secondBtn == null) {
            binding.dividerSt.setVisibility(View.GONE);
        }
        if (firstBtn == null && secondBtn == null) {
            binding.llBtnView.setVisibility(View.GONE);
        }
        if (bodyRunnable != null)
            bodyRunnable.run();

        binding.btnFirst.setOnClickListener(view -> {
            dismiss();
            if (firstBtn != null) {
                if (firstBtn.getBody() != null) {
                    firstBtn.getBody().run();
                }
            }
        });

        binding.btnSecond.setOnClickListener(view -> {
            dismiss();
            if (secondBtn != null) {
                if (secondBtn.getBody() != null)
                    secondBtn.getBody().run();
            }
        });

        tempDialog.setOnDismissListener(dialog -> {
            if (dismissBody != null)
                dismissBody.run();
        });
        tempDialog.show();

        switch (TYPE) {
            case 0: //nothing
                binding.imgType.setVisibility(View.GONE);
                break;

            case 1: //warning
                binding.imgType.setImageResource(R.drawable.ic_warning);
                break;

            case 2: //success
                binding.imgType.setImageResource(R.drawable.ic_success);
                break;

            case 3: //error
                binding.imgType.setImageResource(R.drawable.ic_error);
                break;

            case 4: //info
                binding.imgType.setImageResource(R.drawable.ic_info);
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
