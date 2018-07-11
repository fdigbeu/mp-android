package com.maliprestige.Model;

public class Produit {
    private int id;
    private int produitId;
    private String nom;
    private String descrition;
    private float prixUnitaire;
    private float prixUnitaireTtc;
    private float prixUnitaireGros;
    private float prixExpedition;
    private String image1;
    private String image2;
    private String image3;
    private String date;
    private int quantite;
    private boolean isNouveaute;
    private boolean isReduction;
    private float prixReduction;
    private float prixReductionTtc;
    private String dateReduction;
    private int delaiJourMin;
    private int delaiJourMax;
    private String dateFinReduction;
    private boolean isTailleStandard;
    private String modeEmploi;
    private int typeCoupeId;
    private String typeCoupeLibelle;
    private int typeProduitId;
    private String typeProduitLibelle;
    private int matiereId;
    private String matiereLibelle;
    private int typeMancheId;
    private String typeMancheLibelle;

    @Override
    public String toString() {
        return "{\"id\":\""+id+"\"," +
                "\"produitId\":\""+produitId+"\"," +
                "\"nom\":\""+nom+"\"," +
                "\"descrition\":\""+descrition+"\"," +
                "\"prixUnitaire\":\""+prixUnitaire+"\"," +
                "\"prixUnitaireTtc\":\""+prixUnitaireTtc+"\"," +
                "\"prixUnitaireGros\":\""+prixUnitaireGros+"\"," +
                "\"prixExpedition\":\""+prixExpedition+"\"," +
                "\"image1\":\""+image1+"\"," +
                "\"image2\":\""+image2+"\"," +
                "\"image3\":\""+image3+"\"," +
                "\"date\":\""+date+"\"," +
                "\"quantite\":\""+quantite+"\"," +
                "\"isNouveaute\":\""+isNouveaute+"\"," +
                "\"isReduction\":\""+isReduction+"\"," +
                "\"prixReduction\":\""+prixReduction+"\"," +
                "\"prixReductionTtc\":\""+prixReductionTtc+"\"," +
                "\"dateReduction\":\""+dateReduction+"\"," +
                "\"delaiJourMin\":\""+delaiJourMin+"\"," +
                "\"delaiJourMax\":\""+delaiJourMax+"\"," +
                "\"dateFinReduction\":\""+dateFinReduction+"\"," +
                "\"isTailleStandard\":\""+isTailleStandard+"\"," +
                "\"modeEmploi\":\""+modeEmploi+"\"," +
                "\"typeCoupeId\":\""+typeCoupeId+"\"," +
                "\"typeCoupeLibelle\":\""+typeCoupeLibelle+"\"," +
                "\"typeProduitId\":\""+typeProduitId+"\"," +
                "\"typeProduitLibelle\":\""+typeProduitLibelle+"\"," +
                "\"matiereId\":\""+matiereId+"\"," +
                "\"matiereLibelle\":\""+matiereLibelle+"\"," +
                "\"typeMancheId\":\""+typeMancheId+"\"," +
                "\"typeMancheLibelle\":"+typeMancheLibelle+"}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public float getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public float getPrixUnitaireTtc() {
        return prixUnitaireTtc;
    }

    public void setPrixUnitaireTtc(float prixUnitaireTtc) {
        this.prixUnitaireTtc = prixUnitaireTtc;
    }

    public float getPrixUnitaireGros() {
        return prixUnitaireGros;
    }

    public void setPrixUnitaireGros(float prixUnitaireGros) {
        this.prixUnitaireGros = prixUnitaireGros;
    }

    public float getPrixExpedition() {
        return prixExpedition;
    }

    public void setPrixExpedition(float prixExpedition) {
        this.prixExpedition = prixExpedition;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public boolean isNouveaute() {
        return isNouveaute;
    }

    public void setNouveaute(boolean nouveaute) {
        isNouveaute = nouveaute;
    }

    public boolean isReduction() {
        return isReduction;
    }

    public void setReduction(boolean reduction) {
        isReduction = reduction;
    }

    public float getPrixReduction() {
        return prixReduction;
    }

    public void setPrixReduction(float prixReduction) {
        this.prixReduction = prixReduction;
    }

    public float getPrixReductionTtc() {
        return prixReductionTtc;
    }

    public void setPrixReductionTtc(float prixReductionTtc) {
        this.prixReductionTtc = prixReductionTtc;
    }

    public String getDateReduction() {
        return dateReduction;
    }

    public void setDateReduction(String dateReduction) {
        this.dateReduction = dateReduction;
    }

    public int getDelaiJourMin() {
        return delaiJourMin;
    }

    public void setDelaiJourMin(int delaiJourMin) {
        this.delaiJourMin = delaiJourMin;
    }

    public int getDelaiJourMax() {
        return delaiJourMax;
    }

    public void setDelaiJourMax(int delaiJourMax) {
        this.delaiJourMax = delaiJourMax;
    }

    public String getDateFinReduction() {
        return dateFinReduction;
    }

    public void setDateFinReduction(String dateFinReduction) {
        this.dateFinReduction = dateFinReduction;
    }

    public boolean isTailleStandard() {
        return isTailleStandard;
    }

    public void setTailleStandard(boolean tailleStandard) {
        isTailleStandard = tailleStandard;
    }

    public String getModeEmploi() {
        return modeEmploi;
    }

    public void setModeEmploi(String modeEmploi) {
        this.modeEmploi = modeEmploi;
    }

    public int getTypeCoupeId() {
        return typeCoupeId;
    }

    public void setTypeCoupeId(int typeCoupeId) {
        this.typeCoupeId = typeCoupeId;
    }

    public String getTypeCoupeLibelle() {
        return typeCoupeLibelle;
    }

    public void setTypeCoupeLibelle(String typeCoupeLibelle) {
        this.typeCoupeLibelle = typeCoupeLibelle;
    }

    public int getTypeProduitId() {
        return typeProduitId;
    }

    public void setTypeProduitId(int typeProduitId) {
        this.typeProduitId = typeProduitId;
    }

    public String getTypeProduitLibelle() {
        return typeProduitLibelle;
    }

    public void setTypeProduitLibelle(String typeProduitLibelle) {
        this.typeProduitLibelle = typeProduitLibelle;
    }

    public int getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(int matiereId) {
        this.matiereId = matiereId;
    }

    public String getMatiereLibelle() {
        return matiereLibelle;
    }

    public void setMatiereLibelle(String matiereLibelle) {
        this.matiereLibelle = matiereLibelle;
    }

    public int getTypeMancheId() {
        return typeMancheId;
    }

    public void setTypeMancheId(int typeMancheId) {
        this.typeMancheId = typeMancheId;
    }

    public String getTypeMancheLibelle() {
        return typeMancheLibelle;
    }

    public void setTypeMancheLibelle(String typeMancheLibelle) {
        this.typeMancheLibelle = typeMancheLibelle;
    }
}
