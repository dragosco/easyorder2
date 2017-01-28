package motacojo.mbds.fr.easyorder30.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Person;

public class PersonItemAdapter extends BaseAdapter {

    private Context context;
    public List<Person> people;
    private View.OnClickListener listener;
    public PersonItemAdapter(Context context, List<Person> people, View.OnClickListener listener) {
        this.context = context;
        this.people = people;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int arg0) {
        return people.get(arg0);
    }

    @Override
    public long getItemId(int arg0) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        View v = convertView;

        PersonViewHolder viewHolder = null;
        if(v==null){
            v = View.inflate(context, R.layout.person_item_list_layout, null);
            viewHolder = new PersonViewHolder();
            viewHolder.imageButtonPhoto = (ImageButton) v.findViewById(R.id.imgBtn_status_list);
            viewHolder.nom_prenom= (TextView)v.findViewById(R.id.tv_user_list);
            viewHolder.connected = (TextView)v.findViewById(R.id.tv_status_list);
            viewHolder.imageButtonDelete = (ImageButton) v.findViewById(R.id.imageButton5);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (PersonViewHolder) v.getTag();
        }
        Person person = people.get(position);
        viewHolder.imageButtonPhoto.setOnClickListener(listener);
        viewHolder.imageButtonPhoto.setTag(person);
        viewHolder.nom_prenom.setText(person.getFullName());
        viewHolder.connected.setText(person.isConnected() ? R.string.status_online : R.string.status_offline);
        if (person.isConnected()) {
            viewHolder.connected.setTextColor(ContextCompat.getColor(context, R.color.colorOnlineUser));
        } else {
            viewHolder.connected.setTextColor(ContextCompat.getColor(context, R.color.colorOfflineUser));
        }
        viewHolder.imageButtonDelete.setOnClickListener(listener);
        viewHolder.imageButtonDelete.setTag(person.getId());
        return v;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    class PersonViewHolder{
        ImageButton imageButtonPhoto;
        TextView nom_prenom;
        TextView connected;
        ImageButton imageButtonDelete;
    }

}
