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

public class StudentMemberItemAdapter extends BaseAdapter {

    private List<StudentMemberItem> list;
    private ListView listview;
    private LruCache<String, BitmapDrawable> mImageCache;

    public StudentMemberItemAdapter(List<StudentMemberItem> list) {
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
        StudentMemberItemAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.student_member_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.student_main_members_item_img);
            holder.name = (TextView) convertView.findViewById(R.id.student_main_members_name);
            holder.email = (TextView) convertView.findViewById(R.id.student_main_members_email);
            convertView.setTag(holder);
        } else {
            holder = (StudentMemberItemAdapter.ViewHolder) convertView.getTag();
        }
        StudentMemberItem news = list.get(position);
        holder.iv.setTag(news.getImageid());
        holder.name.setText(news.getName());
        holder.email.setText(news.getEmail());
//         如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (mImageCache.get(news.getImageid()) != null) {
            holder.iv.setImageDrawable(mImageCache.get(news.getImageid()));
        } else {
            StudentMemberItemAdapter.ImageTask it = new StudentMemberItemAdapter.ImageTask();
            it.execute(news.getImageid());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView name;
        TextView email;
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
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 6; //等于数字n即代表压缩成原来的1/n，当数字小于1时会被当成1

            options.inJustDecodeBounds = false; //该属性表示是否只是处理图片的一些宽高值。当值为true时，decodeFile()方法返回null，但是options里面的outHeight/outWidth等参数还是会得到对应值

            options.inPreferredConfig = Bitmap.Config.RGB_565;

            options.inDither = true; //是否抖动
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
