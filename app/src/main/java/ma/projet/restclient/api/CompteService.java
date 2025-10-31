package ma.projet.restclient.api;

import java.util.List;

import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.entities.CompteList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompteService {

    // liste JSON
    @GET("banque/comptes")
    @Headers("Accept: application/json")
    Call<List<Compte>> getAllCompteJson();

    // liste XML
    @GET("banque/comptes")
    @Headers("Accept: application/xml")
    Call<CompteList> getAllCompteXml();

    // détail
    @GET("banque/comptes/{id}")
    Call<Compte> getCompteById(@Path("id") Long id);

    // ajout
    @POST("banque/comptes")
    Call<Compte> addCompte(@Body Compte compte);

    // update
    @PUT("banque/comptes/{id}")
    Call<Compte> updateCompte(@Path("id") Long id, @Body Compte compte);

    // delete
    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id);
}
