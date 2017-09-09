/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.simonbohnen.socialpaka;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import me.simonbohnen.socialpaka.ui.camera.GraphicOverlay;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private Context context;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay,
                         Context context) {
        mGraphicOverlay = ocrGraphicOverlay;
        this.context = context;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                String wort = item.getValue();
                if(wort.length() > 1 && Character.isUpperCase(wort.charAt(0)) && wort.matches("[A-Za-z-]+") && wort.charAt(wort.length() - 1) != '-') {
                    Log.d("OCR", "Name detected: " + wort);
                    // Show AccountDetailActivity
                    Intent startActivity = new Intent(context, AccountDetailActivity.class);
                    AccountDetailActivity.putName(wort, startActivity);
                    context.startActivity(startActivity);
                } else {
                    Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                }
            }
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}
