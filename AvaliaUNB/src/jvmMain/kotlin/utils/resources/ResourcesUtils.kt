package utils.resources

object ResourcesUtils {
    object FontPaths {
        const val QUICKSAND_BOLD = "fonts/Quicksand_Bold.ttf"
        const val QUICKSAND_SEMI_BOLD = "fonts/Quicksand_SemiBold.ttf"
        const val QUICKSAND_REGULAR = "fonts/Quicksand_Regular.ttf"
    }
    object ImagePaths {
        const val CHECK_MARK_SPARKLES = "images/check_mark_sparkles.svg"
        const val APP_LOGO = "images/logo_avalia_unb.svg"
        const val PERSON = "src/jvmMain/resources/images/person.png"
        const val RATING_PEOPLE = "images/rating_people.svg"
        const val RATING_SUBJECTS = "images/rating_subjects.svg"
        const val STUDENTS_TALKING = "images/students_talking.svg"
        const val GRADE = "images/grade.svg"
        const val REVIEWS = "images/reviews.svg"
    }

    object Strings {
        const val FIRST_PART_APP_TITLE = "Avalia"
        const val SECOND_PART_APP_TITLE = "UNB"
        const val COMPLETE_APP_TITLE = "AvaliaUNB"

        const val INITIALIZING_DATABASE = "Inicializando Banco de Dados"
        const val INITIALIZING_DATABASE_DATA = "Inicializando Dados"
        const val CREATING_DATABASE_SCHEMA = "Criando esquema"

        const val PANEL_TEACHERS_CAPTION_TITLE = "AVALIAÇÕES DE PROFESSORES"
        const val PANEL_TEACHERS_CAPTION = "Veja avaliações de outros estudantes e se informe no período de matrículas!"
        const val PANEL_SUBJECTS_CAPTION_TITLE = "AVALIAÇÕES DE DISCIPLINAS"
        const val PANEL_SUBJECTS_CAPTION = "Saiba quais as melhores matérias para escolher no seu próximo semestre!"
        const val PANEL_STUDENTS_CAPTION_TITLE = "INTERAJA COM ESTUDANTES"
        const val PANEL_STUDENTS_CAPTION = "Comente avaliações de outros estudantes e denuncie avaliações inadequadas!"

        const val LOGIN_TITLE = "Bem-vindo ao AvaliaUNB!"
        const val LOGIN_SUBTITLE = "Melhore sua experiência acadêmica com avaliações e opniões reais de outros estudantes!"
        const val LOGIN_FORM_TITLE = "LOGIN"
        const val LOGIN_NO_ACCOUNT_QUESTION = "Não possui uma conta?"
        const val LOGIN_NO_ACCOUNT_REGISTER = "Registre-se!"
        const val LOGIN_BUTTON = "LOGIN"

        const val REGISTER_FORM_TITLE = "REGISTRE-SE"
        const val REGISTER_BUTTON = "REGISTRAR-SE"

        const val EMAIL_FIELD_TITLE = "E-MAIL"
        const val EMAIL_FIELD_HINT = "Insira seu e-mail"
        const val PASSWORD_FIELD_TITLE = "SENHA"
        const val PASSWORD_FIELD_HINT = "Insira sua senha"
        const val REGISTRATION_NUMBER_FIELD_TITLE = "MATRÍCULA"
        const val REGISTRATION_NUMBER_FIELD_HINT = "Insira sua matrícula"
        const val NAME_FIELD_TITLE = "NOME"
        const val NAME_FIELD_HINT = "Insira seu nome"
        const val COURSE_FIELD_TITLE = "CURSO"
        const val COURSE_FIELD_HINT = "Insira seu curso"

        const val INVALID_REGISTRATION_NUMBER = "* Matrícula inválida"
        const val REGISTRATION_NUMBER_ALREADY_IN_USE = "* Matrícula já cadastrada"
        const val EMAIL_ALREADY_IN_USE = "* Email já cadastrado"
        const val USER_NOT_REGISTERED = " * Usuário não cadastrado"
        const val WRONG_PASSWORD = " * Senha incorreta"

        const val REQUIRED_FIELD = "* Campo obrigatório"
        const val OPTIONAL_FIELD = "(Opcional)"

        const val REGISTER_SUCCESSFUL_TITLE = "Registrado com successo!"
        const val REGISTER_SUCCESSFUL_MESSAGE = "Parabéns! Você já está cadastrado."

        const val BACK_TO_LOGIN_BUTTON = "VOLTAR PARA LOGIN"
        const val BACK_BUTTON = "VOLTAR"

        const val DEPARTMENTS = "Departamentos"
        const val SUBJECTS = "Disciplinas"
        const val CLASSES = "Turmas"
        const val TEACHERS = "Professores"
        const val PROFILE = "Perfil"

        const val LOGOUT_BUTTON = "Sair"

        const val EDIT_PROFILE = "Editar perfil"

        const val EDIT_PROFILE_BUTTON = "EDITAR"
        const val CANCEL_BUTTON_UPPERCASE = "CANCELAR"
        const val CANCEL_BUTTON_LOWERCASE = "Cancelar"
        const val FINISH_BUTTON = "FINALIZAR"
        const val PICK_PROFILE_PIC_BUTTON = "Escolher imagem"
        const val PICK_PROFILE_PIC_FILE_CHOOSER = "Escolha uma imagem"
        const val CONFIRM_BUTTON = "Confirmar"
        const val DELETE_ACCOUNT_BUTTON = "Excluir Conta"

        const val SEARCH_FIELD_HINT = "Pesquisar"

        const val DEPARTMENT_FIELD_PREFIX = "Departamento:"
        const val SEMESTER_FIELD_PREFIX = "Semestre:"
        const val NUM_OF_CLASSES_FIELD_PREFIX = "Número de turmas:"
        const val NAME_FIELD_PREFIX = "Nome:"

        const val SCHEDULE_FIELD_PREFIX = "Hórario:"
        const val LOCATION_FIELD_PREFIX = "Local:"
        const val TEACHER_FIELD_PREFIX = "Professor:"
        const val FILLED_SEATS_FIELD_PREFIX = "Vagas ocupadas:"
        const val SCORE_FIELD_PREFIX = "Pontuação:"

        const val ERROR_UNABLE_TO_LOAD_IMAGE = "Não foi possível carregar a imagem."
        const val ERROR_FILE_DOES_NOT_EXIST = "O arquivo selecionado não existe."

        const val NO_REVIEW_MULTILINE = "Nenhuma\nanálise"
        const val NO_REVIEW = "Nenhuma análise"
        const val NO_REVIEW_PUBLISHED = "Nenhuma análise publicada"
        const val RATINGS = "Avaliações"

        const val TEACHER = "Professor"
        const val SEE_TEACHER_DETAILS = "Ver professor"

        const val SEE_DETAILS = "Ver detalhes"

        const val PUBLISH = "Publicar"

        const val REVIEW_FORM_HINT = "Insira sua análise"

        const val DEFAULT_CLASS_SCHEDULE = "Horário a definir."

        const val PUBLISHED = "publicou:"

        const val LOADING = "Carregando..."

        const val FIELD_ERROR_REVIEW_ALREADY_MADE = "* Usuário já fez uma avaliação"

        const val SCORE = "Nota:"
        val STAR_RATINGS = listOf("⭐⭐⭐⭐⭐", "⭐⭐⭐⭐", "⭐⭐⭐", "⭐⭐", "⭐")

        const val SCHEDULES = "Horários"
        val SCHEDULES_LIST = listOf(
            "08:00 - 08:55", "08:55 - 09:50", "10:00 - 10:55", "10:55 - 11:50", "12:00 - 12:55",
            "12:55 - 13:50", "14:00 - 14:55", "14:55 - 15:50", "16:00 - 16:55", "16:55 - 17:50", "18:00 - 18:55", "19:00 - 19:50",
            "19:00 - 19:50", "19:50 - 20:40", "20:50 - 21:40", "21:40 - 22:30"
        )

        val RATING_FILTERS = listOf(
            ">= 5", ">= 4", ">= 3", ">= 2", ">= 1"
        )

        const val ADMINISTRATOR = "Administrador"

        const val REPORT_REVIEW_FIELD_PREFIX = "Denunciar avaliação: "
        const val USER_MADE_REPORT_FIELD_PREFIX = "Você fez uma denúncia: "
        const val REPORT_REVIEW_FIELD_HINT = "Descreva o motivo da denúncia"
        const val REPORT_ERROR_EMPTY_DESCRIPTION = "* Descrição obrigatória"

        const val GENERAL_TEXT_ALL = "TODOS"
    }
}