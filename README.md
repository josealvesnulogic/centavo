# Centavo — Controle de Despesas Mensais (Jetpack Compose)

App Android em Kotlin com Jetpack Compose e Material 3:
- Tema azul‑marinho (navy) e layout moderno
- Tela inicial com navegação por mês, lista de despesas, status pago/atrasado e resumo no rodapé
- Botão flutuante (+) para adicionar despesa
- Formulário com tipos: Fixa, Parcelada, Única (neste mês), data de vencimento, valor, categoria, lembrete opcional
- Arquitetura simples (ViewModel + StateFlow). Persistência em memória por enquanto (fácil migrar para Room)

Imagens de referência mencionadas pelo autor: image1, image2, image3

## Requisitos
- Android Studio Iguana ou mais recente
- JDK 17
- Min SDK 24, Target 34

## Rodando
1. Abra o projeto no Android Studio
2. Sincronize o Gradle
3. Rode no emulador ou dispositivo

## Próximos passos (sugestões)
- Persistência com Room (substituir o repositório em memória)
- Notificações de lembrete antes do vencimento (WorkManager + AlarmManager)
- Importar/Exportar CSV
- Multi-contas/carteiras e categorias personalizáveis
- Widget de saldo no launcher
- Dark mode refinado e cores dinâmicas (Material You)