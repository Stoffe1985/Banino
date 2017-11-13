package sthlm.sweden.christofferwiregren.banino;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RulesFragment extends android.support.v4.app.Fragment {

    private TextView textView, textRules;



    public RulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView)view.findViewById(R.id.toprules);
        textRules = (TextView)view.findViewById(R.id.txtrules) ;

        textView.setGravity(Gravity.CENTER);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textView.setText(R.string.rules_orangino);
        textRules.setText("Alla deltagare har hand om fyra olika omdömeskort vardera. De är märkta med siffrorna 1-4 och har i tur och ordning följande text; Stämmer inte alls, Stämmer till någon del, Stämmer i huvudsak samt Stämmer mycket bra.\n\n" +
                "I varje omgång är en deltagare i fokus. Någon drar och läser upp ett egenskapskort (dessa finns blandade i sju olika färger för egenskaper/personlighetsdrag vagt indelade efter något slags naturliga drivkrafter -gult för energi, grönt för inre balans, lila för andlighet, orange för relationer osv.) \nnKortet läses högt för alla och samtliga spelare väljer därefter det av sina fyra omdömeskort som allra bäst passar in på aktuell person och uppläst egenskap.\n\nNågra exempel på egenskapskort: Noggrann – Jag är grundlig och omsorgsfull. Sexfixerad – Min tankevärld upptas mycket av sex och erotik. Missunnsam – Jag unnar inte andra att få mer än jag. Tävlingsmänniska – Jag presterar mitt bästa när det är som viktigast. Fantasifull – Jag har en livlig och skapande tanke- och idévärld. Flörtig – Jag skickar lättvindigt ut signaler i erotiskt eller vänskapligt syfte. Generös – Jag delar gärna med mig. (Ja, där kom förresten en egenskap från respektive färgkategori, om nu någon la märke till stora skillnader mellan de olika korten…)\n\nNär alla valt omdömeskort vänds de upp samtidigt och därmed står det alldeles klart vad samtliga tycker om person X rörande detta personlighetsdrag, denna egenskap. Exakt hur Noggrann, Sexfixerad, Missunnsam osv. tycker nu dennes omgivning att X är? Och exakt vad har person X valt för omdömeskort själv? Hur väl känner man sig själv?\n\nSpelet vinns såklart av den som känner sig själv bäst, eller i alla fall lyckas ha bäst överensstämmelse mellan sina egna svar och majoriteten av motståndarnas, när de tycker till om ens person. Mest poäng utdelas såklart till personen i heta stolen om samtliga spelare tycker exakt likadant, något färre om man delar uppfattning med de flesta, än färre poäng eller kanske ingen belöning alls om de flesta eller alla andra har en avvikande åsikt.\nDet blir naturligtvis siffran på det kort som visas i flest exemplar som anger poängen. Poängen förresten, de kommer i form av så kallade belöningsmynt av papp i spelkortens olika färger. Du samlar på dig en packe mynt (dvs. poäng) och flest vinner efter ett antal spelade varv eller egenskapskort.");
    }


}
