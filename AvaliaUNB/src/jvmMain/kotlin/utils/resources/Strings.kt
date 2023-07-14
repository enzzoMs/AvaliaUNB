package utils.resources

object Strings {
    const val APP_TITLE_FIRST_PART = "Avalia"
    const val APP_TITLE_SECOND_PART = "UNB"
    const val APP_TITLE_COMPLETE = "AvaliaUNB"

    const val DATABASE_INITIALIZING = "Inicializando Banco de Dados"
    const val DATABASE_INITIALIZING_DATA = "Inicializando Dados"
    const val DATABASE_CREATING_SCHEMA = "Criando esquema"

    const val PANEL_TEACHERS_CAPTION_TITLE = "AVALIAÇÕES DE PROFESSORES"
    const val PANEL_TEACHERS_CAPTION = "Veja avaliações de outros estudantes e se informe no período de matrículas!"
    const val PANEL_SUBJECTS_CAPTION_TITLE = "AVALIAÇÕES DE DISCIPLINAS"
    const val PANEL_SUBJECTS_CAPTION = "Saiba quais as melhores matérias para escolher no seu próximo semestre!"
    const val PANEL_STUDENTS_CAPTION_TITLE = "INTERAJA COM ESTUDANTES"
    const val PANEL_STUDENTS_CAPTION = "Comente avaliações de outros estudantes e denuncie avaliações inadequadas!"

    const val LOGIN_TITLE = "Bem-vindo ao AvaliaUNB!"
    const val LOGIN_SUBTITLE = "Melhore sua experiência acadêmica com avaliações e opniões reais de outros estudantes!"
    const val LOGIN_FORM_TITLE = "LOGIN"
    const val LOGIN_FORM_BUTTON = "LOGIN"
    const val LOGIN_NO_ACCOUNT_QUESTION = "Não possui uma conta?"
    const val LOGIN_NO_ACCOUNT_REGISTER = "Registre-se!"

    const val REGISTER = "REGISTRE-SE"
    const val REGISTER_SUCCESSFUL_TITLE = "Registrado com successo!"
    const val REGISTER_SUCCESSFUL_MESSAGE = "Parabéns! Você já está cadastrado."
    const val REGISTER_BACK_TO_LOGIN_CAPITALIZED = "VOLTAR PARA LOGIN"

    const val FIELD_TITLE_EMAIL = "E-MAIL"
    const val FIELD_TITLE_PASSWORD = "SENHA"
    const val FIELD_TITLE_REGISTRATION_NUMBER = "MATRÍCULA"
    const val FIELD_TITLE_NAME = "NOME"
    const val FIELD_TITLE_COURSE = "CURSO"

    const val FIELD_HINT_EMAIL = "Insira seu e-mail"
    const val FIELD_HINT_PASSWORD = "Insira sua senha"
    const val FIELD_HINT_REGISTRATION_NUMBER = "Insira sua matrícula"
    const val FIELD_HINT_NAME = "Insira seu nome"
    const val FIELD_HINT_COURSE = "Insira seu curso"
    const val FIELD_HINT_SEARCH = "Pesquisar"
    const val FIELD_HINT_REVIEW = "Insira sua análise"
    const val FIELD_HINT_REVIEW_REPORT = "Descreva o motivo da denúncia"

    const val FIELD_PREFIX_DEPARTMENT = "Departamento:"
    const val FIELD_PREFIX_SEMESTER = "Semestre:"
    const val FIELD_PREFIX_NUM_OF_CLASSES = "Número de turmas:"
    const val FIELD_PREFIX_NAME = "Nome:"
    const val FIELD_PREFIX_SCHEDULE = "Hórario:"
    const val FIELD_PREFIX_LOCATION = "Local:"
    const val FIELD_PREFIX_TEACHER = "Professor:"
    const val FIELD_PREFIX_FILLED_SEATS = "Vagas ocupadas:"
    const val FIELD_PREFIX_SCORE = "Pontuação:"
    const val FIELD_PREFIX_PUBLISHED = "publicou:"
    const val FIELD_PREFIX_RATING = "Nota:"
    const val FIELD_PREFIX_REPORT_REVIEW = "Denunciar avaliação: "
    const val FIELD_PREFIX_REPORT_MADE = "Você fez uma denúncia: "

    const val FIELD_ERROR_INVALID_REGISTRATION_NUMBER = "* Matrícula inválida"
    const val FIELD_ERROR_NOT_REGISTERED_USER = " * Usuário não cadastrado"
    const val FIELD_ERROR_WRONG_PASSWORD = " * Senha incorreta"
    const val FIELD_ERROR_REQUIRED = "* Campo obrigatório"
    const val FIELD_ERROR_REQUIRED_DESCRIPTION = "* Descrição obrigatória"
    const val FIELD_ERROR_ALREADY_MADE_REVIEW = "* Usuário já fez uma avaliação"
    const val FIELD_ERROR_ALREADY_IN_USE_REGISTRATION_NUMBER = "* Matrícula já cadastrada"
    const val FIELD_ERROR_ALREADY_IN_USE_EMAIL = "* Email já cadastrado"

    const val FIELD_INFORMATION_OPTIONAL = "(Opcional)"

    const val ERROR_UNABLE_TO_LOAD_IMAGE = "Não foi possível carregar a imagem."
    const val ERROR_FILE_DOES_NOT_EXIST = "O arquivo selecionado não existe."

    const val NO_REVIEW_MULTILINE = "Nenhuma\nanálise"
    const val NO_REVIEW = "Nenhuma análise"
    const val NO_REVIEW_PUBLISHED = "Nenhuma análise publicada"

    const val CAPITALIZED_EDIT = "EDITAR"
    const val CAPITALIZED_FINISH = "FINALIZAR"
    const val CAPITALIZED_CANCEL = "CANCELAR"
    const val CAPITALIZED_BACK = "VOLTAR"
    const val CAPITALIZED_ALL = "TODOS"

    const val DEPARTMENTS = "Departamentos"
    const val SUBJECTS = "Disciplinas"
    const val CLASSES = "Turmas"
    const val TEACHERS = "Professores"
    const val TEACHER = "Professor"
    const val ADMINISTRATOR = "Administrador"
    const val SCHEDULES = "Horários"
    const val PROFILE = "Perfil"
    const val CANCEL = "Cancelar"
    const val LOGOUT = "Sair"
    const val CONFIRM = "Confirmar"
    const val RATINGS = "Avaliações"
    const val LOADING = "Carregando..."

    const val ACTION_PICK_IMAGE = "Escolher imagem"
    const val ACTION_DELETE_ACCOUNT = "Excluir Conta"
    const val ACTION_EDIT_PROFILE = "Editar perfil"
    const val ACTION_PUBLISH = "Publicar"
    const val ACTION_SEE_DETAILS = "Ver detalhes"
    const val ACTION_SEE_DETAILS_TEACHER = "Ver professor"

    const val DEFAULT_CLASS_SCHEDULE = "Horário a definir."

    val LIST_STAR_RATINGS = listOf("⭐⭐⭐⭐⭐", "⭐⭐⭐⭐", "⭐⭐⭐", "⭐⭐", "⭐")
    val LIST_RATING_FILTERS = listOf(">= 5", ">= 4", ">= 3", ">= 2", ">= 1")
    val LIST_SCHEDULES = listOf(
        "08:00 - 08:55", "08:55 - 09:50", "10:00 - 10:55", "10:55 - 11:50", "12:00 - 12:55",
        "12:55 - 13:50", "14:00 - 14:55", "14:55 - 15:50", "16:00 - 16:55", "16:55 - 17:50", "18:00 - 18:55", "19:00 - 19:50",
        "19:00 - 19:50", "19:50 - 20:40", "20:50 - 21:40", "21:40 - 22:30"
    )
}