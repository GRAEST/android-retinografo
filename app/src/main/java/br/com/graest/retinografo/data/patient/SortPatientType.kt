package br.com.graest.retinografo.data.patient

enum class SortPatientType {
    PATIENT_NAME,
    PATIENT_AGE;

    companion object {
        fun stringfy(param: SortPatientType): String {
            return if (param == PATIENT_NAME) {
                "Nome do Paciente"
            } else {
                "Idade do Paciente"
            }
        }
    }
}