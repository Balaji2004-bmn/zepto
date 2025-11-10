package com.app.zepto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private Context context;
    private List<Address> addressList;
    private AddressClickListener clickListener;

    public interface AddressClickListener {
        void onAddressClick(Address address);
        void onEditClick(Address address);
        void onDeleteClick(Address address);
        void onSetDefaultClick(Address address);
    }

    public AddressAdapter(Context context, List<Address> addressList, AddressClickListener clickListener) {
        this.context = context;
        this.addressList = addressList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);

        holder.tvName.setText(address.getFullName());
        holder.tvMobile.setText(address.getMobile());
        holder.tvAddress.setText(address.getCompleteAddress());
        holder.tvAddressType.setText(address.getAddressType());

       
        if (address.isDefault()) {
            holder.tvDefault.setVisibility(View.VISIBLE);
        } else {
            holder.tvDefault.setVisibility(View.GONE);
        }

      
        holder.itemView.setOnClickListener(v -> clickListener.onAddressClick(address));
        holder.btnEdit.setOnClickListener(v -> clickListener.onEditClick(address));
        holder.btnDelete.setOnClickListener(v -> clickListener.onDeleteClick(address));

       
        holder.itemView.setOnLongClickListener(v -> {
            if (!address.isDefault()) {
                clickListener.onSetDefaultClick(address);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return addressList != null ? addressList.size() : 0;
    }

    public void updateAddressList(List<Address> newAddressList) {
        this.addressList = newAddressList;
        notifyDataSetChanged();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile, tvAddress, tvAddressType, tvDefault;
        ImageButton btnEdit, btnDelete;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvAddressType = itemView.findViewById(R.id.tvAddressType);
            tvDefault = itemView.findViewById(R.id.tvDefault);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
