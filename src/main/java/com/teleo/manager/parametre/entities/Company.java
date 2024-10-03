package com.teleo.manager.parametre.entities;

import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
public class Company extends BaseEntity<Company, CompanyRequest> {

    private static final String ENTITY_NAME = "COMPANY";
    private static final String MODULE_NAME = "COMPANY_MODULE";

    @Column(nullable = false, unique = true)
    private String name;

    private String sigle;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    private String site;

    private String telephone2;

    private String adresse;

    private String ville;

    private String bp;

    private String logo;

    @Column(nullable = false, length = 5000)
    private String enteteGauche;

    @Column(nullable = false, length = 5000)
    private String enteteDroite;

    @Column(nullable = false, length = 5000)
    private String piedPage;

    @Override
    public void update(Company source) {
        this.name = source.getName();
        this.sigle = source.getSigle();
        this.email = source.getEmail();
        this.telephone = source.getTelephone();
        this.site = source.getSite();
        this.telephone2 = source.getTelephone2();
        this.adresse = source.getAdresse();
        this.ville = source.getVille();
        this.bp = source.getBp();
        this.logo = source.getLogo();
        this.enteteGauche = source.getEnteteGauche();
        this.enteteDroite = source.getEnteteDroite();
        this.piedPage = source.getPiedPage();
    }

    @Override
    public boolean equalsToDto(CompanyRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = name.equals(source.getName()) &&
                (sigle == null ? source.getSigle() == null : sigle.equals(source.getSigle())) &&
                email.equals(source.getEmail()) &&
                (telephone == null ? source.getTelephone() == null : telephone.equals(source.getTelephone())) &&
                (site == null ? source.getSite() == null : site.equals(source.getSite())) &&
                (telephone2 == null ? source.getTelephone2() == null : telephone2.equals(source.getTelephone2())) &&
                (adresse == null ? source.getAdresse() == null : adresse.equals(source.getAdresse())) &&
                (ville == null ? source.getVille() == null : ville.equals(source.getVille())) &&
                (bp == null ? source.getBp() == null : bp.equals(source.getBp())) &&
                (logo == null ? source.getLogo() == null : logo.equals(source.getLogo())) &&
                enteteGauche.equals(source.getEnteteGauche()) &&
                enteteDroite.equals(source.getEnteteDroite()) &&
                piedPage.equals(source.getPiedPage());

        return areFieldsEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", sigle='" + sigle + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", site='" + site + '\'' +
                ", telephone2='" + telephone2 + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", bp='" + bp + '\'' +
                ", logo='" + logo + '\'' +
                ", enteteGauche='" + enteteGauche + '\'' +
                ", enteteDroite='" + enteteDroite + '\'' +
                ", piedPage='" + piedPage + '\'' +
        '}';
    }
}
