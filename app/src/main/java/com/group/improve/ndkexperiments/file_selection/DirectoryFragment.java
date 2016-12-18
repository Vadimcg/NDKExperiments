package com.group.improve.ndkexperiments.file_selection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.group.improve.ndkexperiments.FileInfoActivity;
import com.group.improve.ndkexperiments.FileSelectionActivity;
import com.group.improve.ndkexperiments.R;

public class DirectoryFragment extends Fragment {

    private View fragmentView;
    private boolean receiverRegistered = false;
    private File currentDir;


    private ListView listView;
    private ListAdapter listAdapter;
    private TextView emptyView;

    private DocumentSelectActivityDelegate delegate;

    private static String title_ = "";
    private ArrayList<ListItem> items = new ArrayList<>();
    private ArrayList<HistoryEntry> history = new ArrayList<>();
    private long sizeLimit = 1024 * 1024 * 1024;

    private class HistoryEntry {
        int scrollItem, scrollOffset;
        File dir;
        String title;
    }

    public  interface DocumentSelectActivityDelegate {
         void updateToolBarName(String name);
    }


    public boolean onBackPressed_() {

        if (history.size() > 0) {

            HistoryEntry he = history.remove(history.size() - 1);

            title_ = he.title;
            updateName(title_);

            if (he.dir != null) {
                listFiles(he.dir);
            } else {
                listRoots();
            }

            listView.setSelectionFromTop(he.scrollItem, he.scrollOffset);
            return false;

        } else {
            return true;
        }
    }

    private void updateName(String title_) {
        if (delegate != null) {
            delegate.updateToolBarName(title_);
        }
    }

    public void onFragmentDestroy() {
        try {
            if (receiverRegistered) {
                getActivity().unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        if (currentDir == null) {
                            listRoots();
                        } else {
                            listFiles(currentDir);
                        }
                    } catch (Exception e) {
                        Log.e("tmessages", e.toString());
                    }
                }
            };
            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                listView.postDelayed(r, 1000);
            } else {
                r.run();
            }
        }
    };


    private class ListItem {
        int icon;
        String title;
        String subtitle = "";
        String ext = "";
        String thumb;
        File file;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!receiverRegistered) {
            receiverRegistered = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
            filter.addAction(Intent.ACTION_MEDIA_CHECKING);
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_NOFS);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addAction(Intent.ACTION_MEDIA_SHARED);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            filter.addDataScheme("file");

            getActivity().registerReceiver(receiver, filter);
        }

        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.document_select_layout,
                    container, false);

            listAdapter = new ListAdapter(getActivity());
            emptyView = (TextView) fragmentView
                    .findViewById(R.id.searchEmptyView);
            emptyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            listView = (ListView) fragmentView.findViewById(R.id.listView);
            listView.setEmptyView(emptyView);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i < 0 || i >= items.size()) {
                        return;
                    }

                    ListItem item = items.get(i);
                    File file = item.file;

                    if (file == null) {

                        HistoryEntry he = history.remove(history.size() - 1);
                        title_ = he.title;
                        updateName(title_);

                        if (he.dir != null) {
                            listFiles(he.dir);
                        } else {
                            listRoots();
                        }

                        listView.setSelectionFromTop(he.scrollItem, he.scrollOffset);

                    } else if (file.isDirectory()) {

                        HistoryEntry he = new HistoryEntry();
                        he.scrollItem = listView.getFirstVisiblePosition();
                        he.scrollOffset = listView.getChildAt(0).getTop();
                        he.dir = currentDir;
                        he.title = title_.toString();
                        updateName(title_);

                        if (!listFiles(file)) {
                            return;
                        }

                        history.add(he);
                        title_ = item.title;
                        updateName(title_);
                        listView.setSelection(0);

                    } else {

                        if (!file.canRead()) {
                            showErrorBox(getString(R.string.file_manager_error_access));
                            return;
                        }

                        if (sizeLimit != 0) {
                            if (file.length() > sizeLimit) {
                                showErrorBox(getString(R.string.file_manager_error_access));
                                return;
                            }
                        }

                        if (file.length() == 0) {
                            return;
                        }
                        else {
                            showFileInfoActivity(file.getAbsolutePath());
                        }

                    }
                }
            });

            listRoots();
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        return fragmentView;
    }

    private void listRoots() {

        currentDir = null;
        items.clear();

        String extStorage = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        ListItem ext = new ListItem();

        if (Build.VERSION.SDK_INT < 9
                || Environment.isExternalStorageRemovable()) {
            ext.title = "SdCard";
        } else {
            ext.title = "InternalStorage";
        }

        //Folder icon
        ext.icon = Build.VERSION.SDK_INT < 9
                || Environment.isExternalStorageRemovable() ? R.drawable.ic_sd_storage
                : R.drawable.ic_folder_open;

        ext.subtitle = getRootSubtitle(extStorage);
        ext.file = Environment.getExternalStorageDirectory();
        items.add(ext);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "/proc/mounts"));
            String line;
            HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
            ArrayList<String> result = new ArrayList<String>();
            String extDevice = null;
            while ((line = reader.readLine()) != null) {
                if ((!line.contains("/mnt") && !line.contains("/storage") && !line
                        .contains("/sdcard"))
                        || line.contains("asec")
                        || line.contains("tmpfs") || line.contains("none")) {
                    continue;
                }
                String[] info = line.split(" ");
                if (!aliases.containsKey(info[0])) {
                    aliases.put(info[0], new ArrayList<String>());
                }
                aliases.get(info[0]).add(info[1]);
                if (info[1].equals(extStorage)) {
                    extDevice = info[0];
                }
                result.add(info[1]);
            }

            reader.close();

            if (extDevice != null) {
                result.removeAll(aliases.get(extDevice));

                for (String path : result) {
                    try {

                        ListItem item = new ListItem();

                        if (path.toLowerCase().contains("sd")) {
                            ext.title = "SdCard";
                        } else {
                            ext.title = "ExternalStorage";
                        }

                        item.icon = R.drawable.ic_sd_storage;
                        item.subtitle = getRootSubtitle(path);
                        item.file = new File(path);
                        items.add(item);

                    } catch (Exception e) {
                        Log.e("tmessages", e.toString());
                    }
                }
            }

        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }

        ListItem fs = new ListItem();
        fs.title = "/";
        fs.subtitle = "SystemRoot";
        fs.icon = R.drawable.ic_folder;
        fs.file = new File("/");
        items.add(fs);

        listAdapter.notifyDataSetChanged();

    }

    private boolean listFiles(File dir) {

        if (!dir.canRead()) {

            if (dir.getAbsolutePath().startsWith(
                    Environment.getExternalStorageDirectory().toString())
                    || dir.getAbsolutePath().startsWith("/sdcard")
                    || dir.getAbsolutePath().startsWith("/mnt/sdcard")) {

                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)
                        && !Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED_READ_ONLY)) {

                    currentDir = dir;
                    items.clear();

                    String state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_SHARED.equals(state)) {
                        emptyView.setText("UsbActive");
                    } else {
                        emptyView.setText("NotMounted");
                    }

                    clearDrawableAnimation(listView);
                    // scrolling = true;
                    listAdapter.notifyDataSetChanged();
                    return true;
                }
            }
            showErrorBox(getString(R.string.file_manager_error_access));
            return false;
        }

        emptyView.setText("NoFiles");
        File[] files;

        try {
            files = dir.listFiles();
        } catch (Exception e) {
            showErrorBox(e.getLocalizedMessage());
            return false;
        }
        if (files == null) {
            showErrorBox("UnknownError");
            return false;
        }

        currentDir = dir;
        items.clear();

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });


        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }

            ListItem item = new ListItem();
            item.title = file.getName();
            item.file = file;

            //if this is a folder
            if (file.isDirectory()) {
                item.icon = R.drawable.ic_folder;
                item.subtitle = "Folder";
            }//or file
            else {
                String[] sp =  file.getName().split("\\.");
                item.ext = sp.length > 1 ? sp[sp.length - 1] : "?";
                item.subtitle = formatFileSize(file.length());

                item.thumb = file.getAbsolutePath();
                item.icon = R.drawable.ic_file;
            }

            items.add(item);
        }

        ListItem item = new ListItem();
        item.title = "..";
        item.subtitle = "Folder";
        item.icon = R.drawable.ic_folder_open;
        item.file = null;
        items.add(0, item);
        clearDrawableAnimation(listView);
        listAdapter.notifyDataSetChanged();
        return true;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    public static void clearDrawableAnimation(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        Drawable drawable = null;
        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }

    public void showErrorBox(String error) {
        if (getActivity() == null) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.file_manager_selection_title))
                .setMessage(error).setPositiveButton("OK", null).show();
    }

    private String getRootSubtitle(String path) {
        StatFs stat = new StatFs(path);
        long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
        long free = (long) stat.getAvailableBlocks()
                * (long) stat.getBlockSize();
        if (total == 0) {
            return "";
        }
        return "Free " + formatFileSize(free) + " of " + formatFileSize(total);
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public int getItemViewType(int pos) {
            return items.get(pos).subtitle.length() > 0 ? 0 : 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextDetailDocumentsCell(mContext);
            }
            ListItem item = items.get(position);
            if (item.icon != 0) {
                ((TextDetailDocumentsCell) convertView)
                        .setTextAndValueAndTypeAndThumb(item.title,
                                item.subtitle, null, null, item.icon);
            } else {
                String type = item.ext.toUpperCase().substring(0,
                        Math.min(item.ext.length(), 4));


                ((TextDetailDocumentsCell) convertView)
                        .setTextAndValueAndTypeAndThumb(item.title,
                                item.subtitle, type, item.thumb, 0);
            }

            return convertView;
        }
    }


    private void showFileInfoActivity(String path){
        Intent intent=new Intent(this.getContext(),FileInfoActivity.class);
        intent.putExtra(FileSelectionActivity.PATH,path);
        this.getContext().startActivity(intent);
    }



}
