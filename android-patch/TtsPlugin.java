package com.vocab3500.app;

import android.speech.tts.TextToSpeech;
import android.os.Build;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.Locale;

/**
 * 轻量原生 TTS 桥：Android WebView 不实现 Web Speech API，
 * 用系统 TextToSpeech 顶替，保证单词朗读可用且即点即播。
 */
@CapacitorPlugin(name = "NativeTts")
public class TtsPlugin extends Plugin {

    private TextToSpeech tts;
    private boolean ready = false;
    private String pending = null;

    @Override
    public void load() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                ready = true;
                tts.setLanguage(Locale.US);
                tts.setSpeechRate(0.95f);
                tts.setPitch(1.0f);
                if (pending != null) {
                    speakNow(pending);
                    pending = null;
                }
            }
        });
    }

    private void speakNow(String text) {
        if (tts == null) return;
        tts.setLanguage(Locale.US);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "vocab3500");
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @PluginMethod
    public void speak(PluginCall call) {
        String text = call.getString("text", "");
        if (text == null || text.length() == 0) {
            call.resolve();
            return;
        }
        if (ready) speakNow(text);
        else pending = text;
        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        pending = null;
        if (tts != null) tts.stop();
        call.resolve();
    }

    @Override
    protected void handleOnDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.handleOnDestroy();
    }
}
