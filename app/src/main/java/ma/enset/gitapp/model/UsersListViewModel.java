package ma.enset.gitapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ma.enset.gitapp.R;

public class UsersListViewModel extends ArrayAdapter<GitUser> {
    private List<GitUser> users;
    private int resource;
    public UsersListViewModel(@NonNull Context context, int resource,List<GitUser> data) {
        super(context, resource,data);
        this.users = data;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem==null){
            listViewItem= LayoutInflater.from(getContext()).inflate(resource,parent,false);
        }
        ImageView imageView = listViewItem.findViewById(R.id.imageViewUser);
        TextView textViewLogin = listViewItem.findViewById(R.id.textViewLogin);
        TextView textViewUrl = listViewItem.findViewById(R.id.textViewUrl);
        textViewLogin.setText(users.get(position).login);
        textViewUrl.setText(users.get(position).htmlUrl);
        try {
            URL url = new URL(users.get(position).avatarUser);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listViewItem;
    }
}
