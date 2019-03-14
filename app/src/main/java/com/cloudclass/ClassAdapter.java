package com.cloudclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ClassAdapter extends BaseAdapter {

    private List<ClassMain> list;
    private ListView listview;
    private LruCache<String, BitmapDrawable> mImageCache;

    public ClassAdapter(List<ClassMain> list) {
        super();
        this.list = list;

        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (listview == null) {
            listview = (ListView) parent;
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.class_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.class_image);
            holder.title = (TextView) convertView.findViewById(R.id.class_name);
            holder.summary = (TextView) convertView.findViewById(R.id.course);
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.teacher = (TextView) convertView.findViewById(R.id.teacher);
            holder.iscreater = (TextView) convertView.findViewById(R.id.iscreater);
            holder.profile = (TextView) convertView.findViewById(R.id.profile);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassMain news = list.get(position);
        holder.title.setText(news.getClassname());
        holder.summary.setText(news.getCourse());
        holder.iv.setTag(news.getImageid());
        holder.code.setText(news.getCode());
        holder.teacher.setText(news.getTeacher());
        holder.iscreater.setText(news.getIscreater());
        holder.profile.setText(news.getProfile());
//         如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (mImageCache.get(news.getImageid()) != null) {
            holder.iv.setImageDrawable(mImageCache.get(news.getImageid()));
        } else {
            ImageTask it = new ImageTask();
            it.execute(news.getImageid());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView title, summary, code, teacher, iscreater, profile;
    }

    class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {

        private String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadImage();
            BitmapDrawable db = new BitmapDrawable(listview.getResources(),
                    bitmap);
            // 如果本地还没缓存该图片，就缓存
            if (mImageCache.get(imageUrl) == null) {
                mImageCache.put(imageUrl, db);
            }
            return db;
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            // 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
            ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
            if (iv != null && result != null) {
                iv.setImageDrawable(result);
            }
        }

        /**
         * 根据url从网络上下载图片
         *
         * @return
         */
        private Bitmap downloadImage() {
            HttpURLConnection con = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }

            return bitmap;
        }

    }
}
