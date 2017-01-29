package motacojo.mbds.fr.easyorder30.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Buzz;

public class NotificationItemAdapter extends BaseAdapter {

    private Context context;
    public List<Buzz> buzzes;

    public NotificationItemAdapter(Context context, List<Buzz> buzzes) {
        this.context = context;
        this.buzzes = buzzes;
    }

    @Override
    public int getCount() {
        return this.buzzes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.buzzes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        NoficationViewHolder viewHolder = null;
        if(v == null){
            v = View.inflate(context, R.layout.notifications_item_list_layout, null);
            viewHolder = new NoficationViewHolder();

            viewHolder.sender = (TextView)v.findViewById(R.id.tv_senderBuzz_itemList);
            viewHolder.date = (TextView)v.findViewById(R.id.tv_datetimeBuzz_itemList);
            viewHolder.message = (TextView)v.findViewById(R.id.tv_messageBuzz_itemList);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (NoficationViewHolder) v.getTag();
        }

        Buzz buzz = buzzes.get(position);
        viewHolder.sender.setText(context.getString(R.string.buzz_sender) + " " + buzz.getSender().getFullName());
        viewHolder.date.setText(context.getString(R.string.buzz_when) + " " + buzz.getSentDate().toString());
        viewHolder.message.setText(context.getString(R.string.buzz_message) + " " + buzz.getMessage());
        return v;
    }

    class NoficationViewHolder {
        TextView sender;
        TextView date;
        TextView message;
    }
}
