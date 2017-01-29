package motacojo.mbds.fr.easyorder30.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Product;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class OrderProductItemAdapter extends BaseAdapter {

    private Context context;
    //Pour l'affichage des produits, on se sert de l'id du produit et de la quantité, d'où le String[]
    public List<String[]> products;

    public OrderProductItemAdapter(Context context, List<String[]> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return this.products.size();
    }

    @Override
    public Object getItem(int position) {
        return this.products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ProductViewHolder viewHolder = null;
        if(v == null){
            v = View.inflate(context, R.layout.order_product_item_list_layout, null);
            viewHolder = new ProductViewHolder();
            viewHolder.name         = (TextView)v.findViewById(R.id.tv_productName_itemList);
            viewHolder.quantity     = (TextView)v.findViewById(R.id.tv_product_qty);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (ProductViewHolder) v.getTag();
        }
        String[] productStringArray = products.get(position);
        GlobalVariables gv = (GlobalVariables) context.getApplicationContext();
        Product product = Product.getById(gv, productStringArray[0]);
        viewHolder.name.setText(product.getName());
        viewHolder.quantity.setText(productStringArray[1]);

        return v;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    class ProductViewHolder{
        TextView name;
        TextView quantity;
    }


}
