package localization;

import gui.save_window_params.Savable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationContext implements Savable {
    private final static String PREFIX = "local";

    private final static String LOCALE = "locale";
    private final static String DEFAULT_LOCALE = "ru";

    private Locale currentLocale = Locale.forLanguageTag(DEFAULT_LOCALE);
    private ResourceBundle bundle;
    private final List<LocalizationListener> listeners = new ArrayList<>();

    public LocalizationContext() {
    }

    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("Messages", locale);
        notifyListeners();
    }

    public String getString(String key) {
        return bundle.getString(key);
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void addListener(LocalizationListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (LocalizationListener listener : listeners) {
            listener.onLanguageChanged();
        }
    }

    public void setParams(Map<String, String> params) {
        setLocale(Locale.forLanguageTag(params.get(LOCALE)));
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> currentParams = new HashMap<>();
        currentParams.put(LOCALE, getCurrentLocale().toLanguageTag());
        return currentParams;
    }

    @Override
    public void loadParameters(Map<String, String> params) {
        setParams(params);
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
