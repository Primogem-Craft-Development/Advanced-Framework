package com.primogemstudio.advancedfmk.flutter;

import static com.primogemstudio.advancedfmk.flutter.Events.instances;

public class ViewEvent {
    public static void resize(int width, int height) {
        instances.forEach(i -> {
            i.rect.top = height - i.height;
            i.rect.bottom = height;
        });
    }
}