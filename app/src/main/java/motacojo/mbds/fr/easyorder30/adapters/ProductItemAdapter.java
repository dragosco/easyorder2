package motacojo.mbds.fr.easyorder30.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Product;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;
import motacojo.mbds.fr.easyorder30.utils.ImageDownloaderTask;

public class ProductItemAdapter extends BaseAdapter {

    private Context context;
    public List<Product> products;
    private View.OnClickListener listener;

    public ProductItemAdapter(Context context, List<Product> products, View.OnClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
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
            v = View.inflate(context, R.layout.product_item_list_layout, null);
            viewHolder = new ProductViewHolder();
            viewHolder.name         = (TextView)v.findViewById(R.id.tv_productName_itemList);
            viewHolder.description  = (TextView)v.findViewById(R.id.tv_productDescr_itemList);
            viewHolder.price        = (TextView)v.findViewById(R.id.tv_productPrice_itemList);
            viewHolder.calories     = (TextView)v.findViewById(R.id.tv_productCalories_itemList);
            viewHolder.type         = (TextView)v.findViewById(R.id.tv_productType_itemList);
            viewHolder.discount     = (TextView)v.findViewById(R.id.tv_productDiscount_itemList);
            viewHolder.picture      = (ImageView)v.findViewById(R.id.img_productImage_itemList);
            viewHolder.lessProduct  = (ImageButton) v.findViewById(R.id.img_btn_less_product);
            viewHolder.moreProduct  = (ImageButton) v.findViewById(R.id.img_btn_more_product);
            viewHolder.quantity     = (TextView)v.findViewById(R.id.tv_product_qty);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (ProductViewHolder) v.getTag();
        }
        Product product = products.get(position);
        viewHolder.name.setText(product.getName());
        viewHolder.description.setText(product.getDescription());
        viewHolder.price.setText("Prix : " + product.getPrice() + " €");
        viewHolder.calories.setText("Calories : " + product.getCalories());
        viewHolder.type.setText("Type : " + product.getType());
        viewHolder.discount.setText("Discount : - " + product.getDiscount() + "%");
        if (viewHolder.picture != null) {
            new ImageDownloaderTask(viewHolder.picture).execute(product.getPicture());
        }
        viewHolder.lessProduct.setOnClickListener(listener);
        viewHolder.lessProduct.setTag(R.id.img_btn_less_product, product.getId());
        viewHolder.moreProduct.setOnClickListener(listener);
        viewHolder.moreProduct.setTag(R.id.img_btn_more_product, product.getId());

        GlobalVariables gv = (GlobalVariables) context.getApplicationContext();
        String quantity = String.valueOf(gv.isOnOrderInProgress(product.getId()) ? gv.getOrderInProgressQty(product.getId()) : 0);
        viewHolder.quantity.setTag(R.id.tv_product_qty, product.getId());
        viewHolder.quantity.setText(quantity);

        return v;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    class ProductViewHolder{
        TextView name;
        TextView description;
        TextView price;
        TextView calories;
        TextView type;
        TextView discount;
        ImageView picture;
        ImageButton lessProduct;
        ImageButton moreProduct;
        TextView quantity;
    }
}
