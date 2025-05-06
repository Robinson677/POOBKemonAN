package domain;

import java.util.EnumMap;
import java.util.Map;

/**
 * Utilidad para consultar la efectividad de tipos.
 */
class TypeEffectivity {
    private static final Map<TypePoobKemon, Map<TypePoobKemon, Double>> tabla =
            new EnumMap<>(TypePoobKemon.class);

    static {
        for (TypePoobKemon atk : TypePoobKemon.values()) {
            Map<TypePoobKemon, Double> m = new EnumMap<>(TypePoobKemon.class);
            for (TypePoobKemon def : TypePoobKemon.values()) {
                m.put(def, 1.0);
            }
            tabla.put(atk, m);
        }

        //ACERO
        setEfectividad(TypePoobKemon.ACERO, 0.5, TypePoobKemon.ACERO, TypePoobKemon.AGUA, TypePoobKemon.ELECTRICO, TypePoobKemon.FUEGO);
        setEfectividad(TypePoobKemon.ACERO, 2.0, TypePoobKemon.HADA, TypePoobKemon.HIELO, TypePoobKemon.ROCA);

        //AGUA
        setEfectividad(TypePoobKemon.AGUA, 0.5, TypePoobKemon.AGUA, TypePoobKemon.DRAGON, TypePoobKemon.PLANTA);
        setEfectividad(TypePoobKemon.AGUA, 2.0, TypePoobKemon.FUEGO, TypePoobKemon.ROCA, TypePoobKemon.TIERRA);

        //BICHO
        setEfectividad(TypePoobKemon.BICHO, 0.5, TypePoobKemon.ACERO, TypePoobKemon.FANTASMA, TypePoobKemon.FUEGO,
                TypePoobKemon.HADA, TypePoobKemon.LUCHA, TypePoobKemon.VENENO, TypePoobKemon.VOLADOR);
        setEfectividad(TypePoobKemon.BICHO, 2.0, TypePoobKemon.PLANTA, TypePoobKemon.PSIQUICO, TypePoobKemon.SINIESTRO);

        //DRAGON
        setEfectividad(TypePoobKemon.DRAGON, 0.0, TypePoobKemon.HADA);
        setEfectividad(TypePoobKemon.DRAGON, 0.5, TypePoobKemon.ACERO);
        setEfectividad(TypePoobKemon.DRAGON, 2.0, TypePoobKemon.ACERO);

        //ELECTRICO
        setEfectividad(TypePoobKemon.ELECTRICO, 0.0, TypePoobKemon.HADA);
        setEfectividad(TypePoobKemon.ELECTRICO, 0.5, TypePoobKemon.DRAGON, TypePoobKemon.ELECTRICO, TypePoobKemon.PLANTA);
        setEfectividad(TypePoobKemon.ELECTRICO, 2.0, TypePoobKemon.AGUA, TypePoobKemon.VOLADOR);

        //FANTASMA
        setEfectividad(TypePoobKemon.FANTASMA, 0.0, TypePoobKemon.NORMAL);
        setEfectividad(TypePoobKemon.FANTASMA, 0.5, TypePoobKemon.SINIESTRO);
        setEfectividad(TypePoobKemon.FANTASMA, 2.0, TypePoobKemon.FANTASMA, TypePoobKemon.PSIQUICO);

        //FUEGO
        setEfectividad(TypePoobKemon.FUEGO, 0.5, TypePoobKemon.AGUA, TypePoobKemon.DRAGON, TypePoobKemon.FUEGO, TypePoobKemon.ROCA);
        setEfectividad(TypePoobKemon.FUEGO, 2.0, TypePoobKemon.ACERO, TypePoobKemon.BICHO, TypePoobKemon.HIELO, TypePoobKemon.PLANTA);

        //HADA
        setEfectividad(TypePoobKemon.HADA, 0.5, TypePoobKemon.ACERO, TypePoobKemon.FUEGO, TypePoobKemon.VENENO);
        setEfectividad(TypePoobKemon.HADA, 2.0, TypePoobKemon.DRAGON, TypePoobKemon.LUCHA, TypePoobKemon.SINIESTRO);

        //HIELO
        setEfectividad(TypePoobKemon.HIELO, 0.5, TypePoobKemon.ACERO, TypePoobKemon.AGUA, TypePoobKemon.FUEGO, TypePoobKemon.HIELO);
        setEfectividad(TypePoobKemon.HIELO, 2.0, TypePoobKemon.DRAGON, TypePoobKemon.PLANTA, TypePoobKemon.TIERRA, TypePoobKemon.VOLADOR);

        //LUCHA
        setEfectividad(TypePoobKemon.LUCHA, 0.0, TypePoobKemon.FANTASMA);
        setEfectividad(TypePoobKemon.LUCHA, 0.5, TypePoobKemon.BICHO, TypePoobKemon.HADA, TypePoobKemon.PSIQUICO, TypePoobKemon.VENENO, TypePoobKemon.VOLADOR);
        setEfectividad(TypePoobKemon.LUCHA, 2.0, TypePoobKemon.ACERO, TypePoobKemon.HIELO, TypePoobKemon.NORMAL, TypePoobKemon.ROCA, TypePoobKemon.SINIESTRO);

        //NORMAL
        setEfectividad(TypePoobKemon.NORMAL, 0.0, TypePoobKemon.FANTASMA);
        setEfectividad(TypePoobKemon.NORMAL, 0.5, TypePoobKemon.ACERO, TypePoobKemon.ROCA);

        //PLANTA
        setEfectividad(TypePoobKemon.PLANTA, 0.5, TypePoobKemon.ACERO, TypePoobKemon.BICHO, TypePoobKemon.DRAGON, TypePoobKemon.FUEGO, TypePoobKemon.PLANTA, TypePoobKemon.VENENO, TypePoobKemon.VOLADOR);
        setEfectividad(TypePoobKemon.PLANTA, 2.0, TypePoobKemon.AGUA, TypePoobKemon.ROCA, TypePoobKemon.TIERRA);

        //PSIQUICO
        setEfectividad(TypePoobKemon.PSIQUICO, 0.0, TypePoobKemon.SINIESTRO);
        setEfectividad(TypePoobKemon.PSIQUICO, 0.5, TypePoobKemon.ACERO, TypePoobKemon.PSIQUICO);
        setEfectividad(TypePoobKemon.PSIQUICO, 2.0, TypePoobKemon.LUCHA, TypePoobKemon.VENENO);

        //ROCA
        setEfectividad(TypePoobKemon.ROCA, 0.5, TypePoobKemon.ACERO, TypePoobKemon.LUCHA, TypePoobKemon.TIERRA);
        setEfectividad(TypePoobKemon.ROCA, 2.0, TypePoobKemon.BICHO, TypePoobKemon.FUEGO, TypePoobKemon.HIELO, TypePoobKemon.VOLADOR);

        //SINIESTRO
        setEfectividad(TypePoobKemon.SINIESTRO, 0.5, TypePoobKemon.HADA, TypePoobKemon.LUCHA, TypePoobKemon.SINIESTRO);
        setEfectividad(TypePoobKemon.SINIESTRO, 2.0, TypePoobKemon.FANTASMA, TypePoobKemon.PSIQUICO);


        //TIERRA
        setEfectividad(TypePoobKemon.TIERRA, 0.0, TypePoobKemon.VOLADOR);
        setEfectividad(TypePoobKemon.TIERRA, 0.5, TypePoobKemon.BICHO, TypePoobKemon.PLANTA);
        setEfectividad(TypePoobKemon.TIERRA, 2.0, TypePoobKemon.ACERO, TypePoobKemon.ELECTRICO, TypePoobKemon.FUEGO, TypePoobKemon.ROCA, TypePoobKemon.VENENO);

        //VENENO
        setEfectividad(TypePoobKemon.VENENO, 0.0, TypePoobKemon.ACERO);
        setEfectividad(TypePoobKemon.VENENO, 0.5, TypePoobKemon.FANTASMA, TypePoobKemon.ROCA, TypePoobKemon.TIERRA, TypePoobKemon.VENENO);
        setEfectividad(TypePoobKemon.VENENO, 2.0, TypePoobKemon.HADA, TypePoobKemon.PLANTA);

        //VOLADOR
        setEfectividad(TypePoobKemon.VOLADOR, 0.5, TypePoobKemon.ACERO, TypePoobKemon.ELECTRICO, TypePoobKemon.ROCA);
        setEfectividad(TypePoobKemon.VOLADOR, 2.0, TypePoobKemon.BICHO, TypePoobKemon.LUCHA, TypePoobKemon.PLANTA);

    }

    private static void setEfectividad(TypePoobKemon atk, double mult, TypePoobKemon... defs) {
        Map<TypePoobKemon, Double> m = tabla.get(atk);
        for (TypePoobKemon def : defs) {
            m.put(def, mult);
        }
    }

    public static double get(TypePoobKemon movTipo, TypePoobKemon defTipo) {
        return tabla.get(movTipo).getOrDefault(defTipo, 1.0);
    }
}