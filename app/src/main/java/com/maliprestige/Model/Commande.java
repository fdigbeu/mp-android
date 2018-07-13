package com.maliprestige.Model;

public class Commande {
    private int id;
    private int commandeId;
    private String numeroCommande;
    private float montantCommande;
    private String dateValidation;
    private String dateAnnulation;
    private String dateRetour;
    private String dateFournisseur;
    private String dateExpedition;
    private String dateReception;
    private String dateEnPreparation;
    private String numeroColis;
    private String dateAchat;
    private String statutLibelle;
    private String statutDetail;
    private String libelleDateLivraison;
    private String libelleAdresseLivraison;
    private String libelleAdresseFacturation;
    private String numeroFacture;
    private String modePaiement;
    private float montantRegle;
    private boolean isFactureAcquittee;
    private String dateAcquittement;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public float getMontantCommande() {
        return montantCommande;
    }

    public void setMontantCommande(float montantCommande) {
        this.montantCommande = montantCommande;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(String dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getDateFournisseur() {
        return dateFournisseur;
    }

    public void setDateFournisseur(String dateFournisseur) {
        this.dateFournisseur = dateFournisseur;
    }

    public String getDateExpedition() {
        return dateExpedition;
    }

    public void setDateExpedition(String dateExpedition) {
        this.dateExpedition = dateExpedition;
    }

    public String getDateReception() {
        return dateReception;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public String getDateEnPreparation() {
        return dateEnPreparation;
    }

    public void setDateEnPreparation(String dateEnPreparation) {
        this.dateEnPreparation = dateEnPreparation;
    }

    public String getNumeroColis() {
        return numeroColis;
    }

    public void setNumeroColis(String numeroColis) {
        this.numeroColis = numeroColis;
    }

    public String getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(String dateAchat) {
        this.dateAchat = dateAchat;
    }

    public String getStatutLibelle() {
        return statutLibelle;
    }

    public void setStatutLibelle(String statutLibelle) {
        this.statutLibelle = statutLibelle;
    }

    public String getStatutDetail() {
        return statutDetail;
    }

    public void setStatutDetail(String statutDetail) {
        this.statutDetail = statutDetail;
    }

    public String getLibelleDateLivraison() {
        return libelleDateLivraison;
    }

    public void setLibelleDateLivraison(String libelleDateLivraison) {
        this.libelleDateLivraison = libelleDateLivraison;
    }

    public String getLibelleAdresseLivraison() {
        return libelleAdresseLivraison;
    }

    public void setLibelleAdresseLivraison(String libelleAdresseLivraison) {
        this.libelleAdresseLivraison = libelleAdresseLivraison;
    }

    public String getLibelleAdresseFacturation() {
        return libelleAdresseFacturation;
    }

    public void setLibelleAdresseFacturation(String libelleAdresseFacturation) {
        this.libelleAdresseFacturation = libelleAdresseFacturation;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public float getMontantRegle() {
        return montantRegle;
    }

    public void setMontantRegle(float montantRegle) {
        this.montantRegle = montantRegle;
    }

    public boolean isFactureAcquittee() {
        return isFactureAcquittee;
    }

    public void setFactureAcquittee(boolean factureAcquittee) {
        isFactureAcquittee = factureAcquittee;
    }

    public String getDateAcquittement() {
        return dateAcquittement;
    }

    public void setDateAcquittement(String dateAcquittement) {
        this.dateAcquittement = dateAcquittement;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
