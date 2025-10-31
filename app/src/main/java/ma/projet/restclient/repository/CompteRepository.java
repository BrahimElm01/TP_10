package ma.projet.restclient.repository;

import java.util.List;

import ma.projet.restclient.api.CompteService;
import ma.projet.restclient.config.RetrofitClient;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.entities.CompteList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteRepository {

    private final CompteService compteService;
    private final String format;

    public CompteRepository(String converterType) {
        this.compteService = RetrofitClient.getClient(converterType).create(CompteService.class);
        this.format = converterType;
    }

    public void getAllCompte(Callback<List<Compte>> callback) {
        if ("JSON".equals(format)) {
            Call<List<Compte>> call = compteService.getAllCompteJson();
            call.enqueue(callback);
        } else {
            Call<CompteList> call = compteService.getAllCompteXml();
            call.enqueue(new Callback<CompteList>() {
                @Override
                public void onResponse(Call<CompteList> call, Response<CompteList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Compte> comptes = response.body().getComptes();
                        callback.onResponse(null, Response.success(comptes));
                    } else {
                        callback.onResponse(null, Response.success(null));
                    }
                }

                @Override
                public void onFailure(Call<CompteList> call, Throwable t) {
                    callback.onFailure(null, t);
                }
            });
        }
    }

    public void addCompte(Compte compte, Callback<Compte> callback) {
        compteService.addCompte(compte).enqueue(callback);
    }

    public void updateCompte(Long id, Compte compte, Callback<Compte> callback) {
        compteService.updateCompte(id, compte).enqueue(callback);
    }

    public void deleteCompte(Long id, Callback<Void> callback) {
        compteService.deleteCompte(id).enqueue(callback);
    }
}
