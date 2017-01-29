package motacojo.mbds.fr.easyorder30.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Product;

public class PreparingOrderItemAdapter extends BaseAdapter {

    private Context context;
    public List<Order> orders;

    public PreparingOrderItemAdapter(Context context, List<Order> orders) {
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
            v = View.inflate(context, R.layout.preparing_orders_item_list_layout, null);
            viewHolder = new TakenOrderViewHolder();

            viewHolder.cooker = (TextView)v.findViewById(R.id.tv_orderCooker_itemList);
            viewHolder.waiter = (TextView)v.findViewById(R.id.tv_senderBuzz_itemList);
            viewHolder.totalAmount = (TextView)v.findViewById(R.id.tv_orderAmount_itemList);
            viewHolder.totalDiscount = (TextView)v.findViewById(R.id.tv_orderDiscount_itemList);
            viewHolder.total = (TextView)v.findViewById(R.id.tv_orderTotal_itemList);
            viewHolder.items = (ListView)v.findViewById((R.id.listView_order_items));
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (TakenOrderViewHolder) v.getTag();
        }

        Order order = orders.get(position);
        viewHolder.cooker.setText(order.getCooker().getPrenom());
        viewHolder.waiter.setText(order.getWaiter().getPrenom());
        viewHolder.totalAmount.setText(String.format("%.2f", order.getTotalAmount()) + "€");
        viewHolder.totalDiscount.setText("- " + String.format("%.2f", order.getTotalDiscount()) + "€");
        viewHolder.total.setText(String.format("%.2f", order.getTotal()) + "€");

        HashMap<String, Integer> map = new HashMap<>();
        for (Product p : order.getItems()) {
            addProductToOrder(map, p.getId());
        }

        OrderProductItemAdapter adapter = new OrderProductItemAdapter(v.getContext(), mapToListOfStringArray(map));
        viewHolder.items.setAdapter(adapter);
        viewHolder.items.getLayoutParams().height = 100*map.size();
        return v;
    }

    class TakenOrderViewHolder{
        TextView cooker;
        TextView waiter;
        TextView totalAmount;
        TextView totalDiscount;
        TextView total;
        ListView items;
    }


    public void addProductToOrder(HashMap<String, Integer> map, String productId) {
        if (map.containsKey(productId)) {
            int qty = map.get(productId);
            map.put(productId, qty + 1);
        } else {
            map.put(productId, 1);
        }
    }

    public List<String[]> mapToListOfStringArray(HashMap<String, Integer> map) {
        List<String[]> productsStringArray = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String[] tuple = new String[2];
            tuple[0] = entry.getKey();
            tuple[1] = String.valueOf(entry.getValue());
            productsStringArray.add(tuple);
        }
        return productsStringArray;
    }


}
