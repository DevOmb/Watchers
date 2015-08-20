package com.ombrax.watchers.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devomb.floatingtogglebutton.FloatingToggleButton;
import com.devomb.focusframe.FocusFrame;
import com.devomb.focusframe.FocusFrameTouchHandler;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.ImageUtils;
import com.ombrax.watchers.Utils.StorageUtils;
import com.ombrax.watchers.Views.Button.AnimatedFloatingToggleButton;
import com.ombrax.watchers.Views.Button.LabeledIconButton;

import java.io.File;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class CropImageFragment extends Fragment {


    //region declaration
    //region constant
    public static final int IMAGE_REQUEST = 1;
    //endregion

    //region view
    private PhotoViewAttacher photoViewAttacher;

    private ImageView previewImage;
    private FocusFrame focusFrame;
    private LabeledIconButton saveButton;
    private LabeledIconButton openGalleryButton;
    private AnimatedFloatingToggleButton floatingToggleButton;
    //endregion

    //region inner field
    private String fileName;
    //endregion
    //endregion

    //region create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_image, container, false);

        previewImage = (ImageView) view.findViewById(R.id.fragment_crop_image_preview);
        focusFrame = (FocusFrame) view.findViewById(R.id.fragment_crop_image_focus_frame);
        saveButton = (LabeledIconButton) view.findViewById(R.id.fragment_crop_image_save_button);
        openGalleryButton = (LabeledIconButton) view.findViewById(R.id.fragment_crop_image_open_gallery_button);
        floatingToggleButton = (AnimatedFloatingToggleButton) view.findViewById(R.id.fragment_crop_image_aftb);

        photoViewAttacher = new PhotoViewAttacher(previewImage);
        setupButtons();
        focusFrame.setOnTouchInsideListener(new FocusFrameTouchHandler.DefaultOnTouchInsideListener() {
            @Override
            public void onTouchDown() {
                floatingToggleButton.toggleAnimateVisibility();
            }

            @Override
            public void onTouchUp() {
                floatingToggleButton.toggleAnimateVisibility();
            }
        });

        return view;
    }
    //endregion

    //region helper
    private void setupButtons() {
        openGalleryButton.setIconResource(R.drawable.ic_gallery);
        openGalleryButton.setLabel("Gallery");
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        saveButton.setIconResource(R.drawable.ic_accept);
        saveButton.setLabel("Accept");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCroppedImage();
            }
        });
        saveButton.setEnabled(false);

        floatingToggleButton.setOnToggleChangeListener(new FloatingToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggleChange(boolean isEnabled) {
                focusFrame.setMovable(!isEnabled);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void saveCroppedImage() {
        String path = StorageUtils.saveImageToExternalStorage(fileName, getCroppedBitmap());
        if (path != null) {
            DomainController.getInstance().onThumbnailImageSaved(path);
        }
        getActivity().finish();
    }

    private Bitmap getCroppedBitmap() {
        RectF box = focusFrame.getBox();
        return Bitmap.createBitmap(photoViewAttacher.getVisibleRectangleBitmap(), (int) box.left, (int) box.top, (int) box.width(), (int) box.height());
    }

    private void setImageFromBitmap(String fileName, Bitmap bitmap) {
        this.fileName = fileName;
        previewImage.setImageBitmap(bitmap);
        photoViewAttacher.update();
        saveButton.setEnabled(true);
    }
    //endregion

    //region override
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                String filePath = StorageUtils.getFilePathFromURI(getActivity(), uri);
                Bitmap bitmap = ImageUtils.getScaledBitmap(filePath);
                setImageFromBitmap(StorageUtils.getFileNameFromPath(filePath), bitmap);
            }
        }
    }
    //endregion
}
