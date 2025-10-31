package ma.projet.restclient;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ma.projet.restclient.adapter.CompteAdapter;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.repository.CompteRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements CompteAdapter.OnDeleteClickListener,
        CompteAdapter.OnUpdateClickListener {

    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private RadioGroup formatGroup;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupFormatToggle();
        setupAddButton();

        // charge JSON par défaut
        loadData("JSON");
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        formatGroup = findViewById(R.id.formatGroup);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompteAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFormatToggle() {
        formatGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String fmt = (checkedId == R.id.radioJson) ? "JSON" : "XML";
            loadData(fmt);
        });
    }

    private void setupAddButton() {
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    // ---------- DIALOG ADD ----------
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);

        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);

        builder.setView(dialogView)
                .setTitle("Ajouter un compte")
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String soldeStr = etSolde.getText().toString().trim();
                    if (soldeStr.isEmpty()) {
                        toast("Solde vide");
                        return;
                    }
                    double solde = Double.parseDouble(soldeStr);

                    String type = (typeGroup.getCheckedRadioButtonId() == R.id.radioCourant)
                            ? "COURANT"
                            : "EPARGNE";

                    String today = getToday();
                    Compte c = new Compte(null, solde, type, today);
                    addCompte(c);
                })
                .setNegativeButton("Annuler", null);

        builder.create().show();
    }

    private String getToday() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    private void addCompte(Compte compte) {
        CompteRepository repo = new CompteRepository("JSON");
        repo.addCompte(compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    toast("Compte ajouté");
                    loadData("JSON");
                } else {
                    toast("Erreur lors de l'ajout (HTTP)");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                toast("Erreur réseau ajout : " + t.getMessage());
            }
        });
    }

    // ---------- LOAD DATA ----------
    private void loadData(String format) {
        CompteRepository repo = new CompteRepository(format);
        repo.getAllCompte(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Compte> comptes = response.body();
                    runOnUiThread(() -> adapter.updateData(comptes));
                } else {
                    runOnUiThread(() -> adapter.updateData(null));
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                toast("Erreur chargement : " + t.getMessage());
            }
        });
    }

    // ---------- UPDATE ----------
    @Override
    public void onUpdateClick(Compte compte) {
        showUpdateDialog(compte);
    }

    private void showUpdateDialog(Compte compte) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);

        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);

        // pré-remplir
        etSolde.setText(String.valueOf(compte.getSolde()));
        if ("COURANT".equalsIgnoreCase(compte.getType())) {
            typeGroup.check(R.id.radioCourant);
        } else {
            typeGroup.check(R.id.radioEpargne);
        }

        builder.setView(dialogView)
                .setTitle("Modifier un compte")
                .setPositiveButton("Modifier", (dialog, which) -> {
                    String soldeStr = etSolde.getText().toString().trim();
                    if (soldeStr.isEmpty()) {
                        toast("Solde vide");
                        return;
                    }
                    double solde = Double.parseDouble(soldeStr);

                    String type = (typeGroup.getCheckedRadioButtonId() == R.id.radioCourant)
                            ? "COURANT"
                            : "EPARGNE";

                    compte.setSolde(solde);
                    compte.setType(type);

                    updateCompte(compte);
                })
                .setNegativeButton("Annuler", null);

        builder.create().show();
    }

    private void updateCompte(Compte compte) {
        CompteRepository repo = new CompteRepository("JSON");
        repo.updateCompte(compte.getId(), compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    toast("Compte modifié");
                    loadData("JSON");
                } else {
                    toast("Erreur modif (HTTP)");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                toast("Erreur réseau modif : " + t.getMessage());
            }
        });
    }

    // ---------- DELETE ----------
    @Override
    public void onDeleteClick(Compte compte) {
        confirmDelete(compte);
    }

    private void confirmDelete(Compte compte) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Supprimer ce compte ?")
                .setPositiveButton("Oui", (dialogInterface, i) -> doDelete(compte))
                .setNegativeButton("Non", null)
                .show();
    }

    private void doDelete(Compte compte) {
        CompteRepository repo = new CompteRepository("JSON");
        repo.deleteCompte(compte.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toast("Compte supprimé");
                    loadData("JSON");
                } else {
                    toast("Erreur suppression (HTTP)");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toast("Erreur réseau suppression : " + t.getMessage());
            }
        });
    }

    // ---------- Utils ----------
    private void toast(String msg) {
        runOnUiThread(() ->
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show()
        );
    }
}
