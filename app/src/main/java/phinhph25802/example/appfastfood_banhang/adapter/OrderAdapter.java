package phinhph25802.example.appfastfood_banhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    List<Order> list;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Order> arrayList) {
        this.list = arrayList;
        notifyDataSetChanged();
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        int i = position;
        Order order = list.get(i);
        if (order == null)
            return;
        holder.tvTrangthai.setText(order.getTrangthai());
        holder.tvDate.setText(order.getDate());
        holder.tvTongtien.setText(Utilities.addDots(order.getTongdonhang()) + "đ");
        if (holder.tvTrangthai.getText().toString().equalsIgnoreCase("Chờ xác nhận")) {
//            #FF9800
            holder.lo_bg_status.setBackgroundColor(Color.CYAN);
            holder.img_status.setImageResource(R.drawable.ic_preparing);
            holder.tvTrangthai.setTextColor(Color.CYAN);
        } else if (holder.tvTrangthai.getText().toString().equalsIgnoreCase("Đã thanh toán và chờ xác nhận")) {
//            #8BC34A
            holder.lo_bg_status.setBackgroundColor(Color.CYAN);
            holder.img_status.setImageResource(R.drawable.ic_preparing);
            holder.tvTrangthai.setTextColor(Color.CYAN);
        } else if (holder.tvTrangthai.getText().toString().equalsIgnoreCase("Đang giao hàng")) {
//            #8BC34A
            holder.lo_bg_status.setBackgroundColor(Color.GREEN);
            holder.img_status.setImageResource(R.drawable.ic_shipping);
            holder.tvTrangthai.setTextColor(Color.GREEN);
        } else if (holder.tvTrangthai.getText().toString().equalsIgnoreCase("Đã giao hàng")) {
//            #03A9F4
            holder.lo_bg_status.setBackgroundColor(Color.BLUE);
            holder.img_status.setImageResource(R.drawable.ic_finish);
            holder.tvTrangthai.setTextColor(Color.BLUE);
        } else if (holder.tvTrangthai.getText().toString().equalsIgnoreCase("Đã hủy đơn hàng")) {
            holder.img_status.setImageResource(R.drawable.ic_cancel);
            holder.lo_bg_status.setBackgroundColor(Color.RED);
            holder.tvTrangthai.setTextColor(Color.RED);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrderDetail.class);
                intent.putExtra("id_order", order.getId());
                intent.putExtra("pttt_order", order.getThanhtoan());
                intent.putExtra("trangthai_order", order.getTrangthai());
                intent.putExtra("date_order", order.getDate());
                intent.putExtra("tongtien_order", order.getTongdonhang());
                intent.putExtra("nameUser_order", order.getId_user().getFullname());
                intent.putExtra("emailUser_order", order.getId_user().getEmail());
                intent.putExtra("phoneUser_order", order.getId_user().getPhone());
                intent.putExtra("addressUser_order", order.getId_user().getAddress());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrangthai, tvDate, tvTongtien;
        LinearLayout lo_bg_status;
        ImageView img_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrangthai = itemView.findViewById(R.id.tv_trangthai_order_item);
            tvDate = itemView.findViewById(R.id.tv_date_order_item);
            tvTongtien = itemView.findViewById(R.id.tv_tongtien_order_item);
            lo_bg_status = itemView.findViewById(R.id.lo_bg_status_order_item);
            img_status = itemView.findViewById(R.id.img_status_order_item);
        }
    }
}
