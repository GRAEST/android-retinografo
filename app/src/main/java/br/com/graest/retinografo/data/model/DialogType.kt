package br.com.graest.retinografo.data.model

//ver se isso vai ficar aqui ou se é necessário
enum class DialogType {
    CREATE,
    EDIT;

    companion object {
        fun stringfy(param: DialogType): String {
            return if (param == CREATE) {
                "Create"
            } else {
                "Edit"
            }
        }
    }
}
