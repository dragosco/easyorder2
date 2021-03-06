package motacojo.mbds.fr.easyorder30.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Order;

public class TakenOrderItemAdapter extends BaseAdapter {

    private Context context;
    public List<Order> orders;

    public TakenOrderItemAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return this.orders.size();
    }

    @Override
    public Object getItem(int position) {
        return this.orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        TakenOrderViewHolder viewHolder = null;
        if(v == null){
            v = View.inflate(context, R.layout.taken_orders_item_list_layout, null);
            viewHolder = new TakenOrderViewHolder();

            viewHolder.cooker = (TextView)v.findViewById(R.id.tv_orderCooker_itemList);
            viewHolder.waiter = (TextView)v.findViewById(R.id.tv_orderWaiter_itemList);
            viewHolder.totalAmount = (TextView)v.findViewById(R.id.tv_orderAmount_itemList);
            viewHolder.totalDiscount = (TextView)v.findViewById(R.id.tv_orderDiscount_itemList);
            viewHolder.total = (TextView)v.findViewById(R.id.tv_orderTotal_itemList);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (TakenOrderViewHolder) v.getTag();
        }

        Order order = orders.get(position);
        viewHolder.cooker.setText(order.getCooker().getPrenom());
        viewHolder.waiter.setText(order.getWaiter().getPrenom());
        viewHolder.totalAmount.setText(String.format("%.2f", (double) order.getTotalAmount()) + "€");
        viewHolder.totalDiscount.setText("- " + String.format("%.2f", order.getTotalDiscount()) + "€");
        viewHolder.total.setText(String.format("%.2f", order.getTotal()) + "€");
        return v;
    }

    class TakenOrderViewHolder{
        TextView cooker;
        TextView waiter;
        TextView totalAmount;
        TextView totalDiscount;
        TextView total;
    }
}
