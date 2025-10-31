package ma.projet.restclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.projet.restclient.R;
import ma.projet.restclient.entities.Compte;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Compte compte);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(Compte compte);
    }

    private final List<Compte> comptes;
    private final OnDeleteClickListener onDeleteClickListener;
    private final OnUpdateClickListener onUpdateClickListener;

    public CompteAdapter(OnDeleteClickListener deleteListener,
                         OnUpdateClickListener updateListener) {
        this.comptes = new ArrayList<>();
        this.onDeleteClickListener = deleteListener;
        this.onUpdateClickListener = updateListener;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.bind(compte);
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public void updateData(List<Compte> newComptes) {
        this.comptes.clear();
        if (newComptes != null) {
            this.comptes.addAll(newComptes);
        }
        notifyDataSetChanged();
    }

    class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvSolde, tvType, tvDate;
        View btnDelete, btnEdit;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvSolde = itemView.findViewById(R.id.tvSolde);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        void bind(Compte compte) {
            tvId.setText("ID: " + compte.getId());
            tvSolde.setText(String.format("Solde: %.2f", compte.getSolde()));
            tvType.setText("Type: " + compte.getType());
            tvDate.setText("Date: " + compte.getDateCreation());

            btnDelete.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(compte);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(compte);
                }
            });
        }
    }
}
