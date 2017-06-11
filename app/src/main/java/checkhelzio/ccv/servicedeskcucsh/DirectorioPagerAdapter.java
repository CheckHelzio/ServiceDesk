package checkhelzio.ccv.servicedeskcucsh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class DirectorioPagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs;
    private static String mDependencia;

    DirectorioPagerAdapter(FragmentManager fm, int NumOfTabs, String dependencia) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        mDependencia = dependencia;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Todos";
        }

        switch (mDependencia) {
            case "Rectoría":
                switch (position) {
                    case 1:
                        return "Rectoría";
                    case 2:
                        return "Secretaría Académica";
                    case 3:
                        return "Secretaría Administrativa";
                    case 4:
                        return "Contraloría";
                }
            case "División de Estudios Históricos y Humanos":
                switch (position) {
                    case 1:
                        return "Lenguas Modernas";
                    case 2:
                        return "Filosofía";
                    case 3:
                        return "Geografía y Ordenación Territorial";
                    case 4:
                        return "Historia";
                    case 5:
                        return "Letras";
                }
            case "División de Estudios de la Cultura":
                switch (position) {
                    case 1:
                        return "Lenguas Indígenas";
                    case 2:
                        return "Comunicación Social";
                    case 3:
                        return "Estudios Literarios";
                    case 4:
                        return "Mesoamericanos y Mexicanos";
                }
            case "División de Estudios Jurídicos":
                switch (position) {
                    case 1:
                        return "Derecho Público";
                    case 2:
                        return "Derecho Privado";
                    case 3:
                        return "Derecho Social";
                    case 4:
                        return "Disciplinas Afines al Derecho";
                }
            case "Cátedras":
                switch (position) {
                    case 1:
                        return "Julio Cortázar";
                    case 2:
                        return "José Martí";
                    case 3:
                        return "Èmile Durkheim";
                }
            case "División de Estudios Políticos y Sociales":
                switch (position) {
                    case 1:
                        return "Estudios Internacionales";
                    case 2:
                        return "Sociología";
                    case 3:
                        return "Estudios Políticos";
                    case 4:
                        return "Trabajo Social";
                    case 5:
                        return "Desarrollo Social";
                }
            case "División de Estado Sociedad":
                switch (position) {
                    case 1:
                        return "Educación";
                    case 2:
                        return "Ibéricos y Latinoamericanos";
                    case 3:
                        return "Movimientos Sociales";
                    case 4:
                        return "Socio Urbanos";
                    case 5:
                        return "Estudios del Pacífico";
                }
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        return TabDirectorioContactos.newInstance(position, mDependencia);
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}