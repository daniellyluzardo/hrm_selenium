# Roteiro de Entrevista Simulada — Preparação para Quality Engineer na SAP
### Um guia para quem vai entrevistar (não precisa ter conhecimento técnico)

## Como conduzir isso

- **Tempo total:** 60–90 minutos se usar tudo. As seções são independentes —
  pode pular ou reordenar. Se o tempo for curto, priorize as Seções 2, 3, 6 e 9.
- **Seu papel NÃO é entender as respostas tecnicamente.** Abaixo de cada pergunta
  tem uma caixa **"Preste atenção em"** com palavras, ideias e comparações que uma
  boa resposta deve conter. Você está comparando o que ela disse com essa lista,
  não julgando a lógica técnica em si.
- **Questione (dê um "push back") pelo menos uma vez por seção.** Depois de
  qualquer resposta, escolha uma destas frases e use — isso simula a pressão real
  de uma entrevista:
  - "Explica de novo como se eu não entendesse nada de técnico."
  - "Por que isso importa? E daí?"
  - "Tem certeza?" (mesmo quando a resposta estiver certa — o que se testa aqui é
    a segurança dela, não se ela vai mudar de ideia)
- **Regra do silêncio:** se ela ficar mais de ~15 segundos calada, anote. O hábito
  mais importante sendo treinado aqui é **pensar em voz alta**. Silêncio longo numa
  entrevista de verdade é sinal ruim mesmo quando a resposta final está certa.
- **Pontuação por pergunta**, anote na margem:
  - ✅ tocou nos pontos do "preste atenção em", falou com fluidez
  - 🟡 chegou lá, mas devagar, ou esqueceu de explicar o "por que isso importa"
  - ❌ travou, enrolou sem dizer nada de concreto, ou ficou em silêncio
  No final, cada 🟡/❌ vira um tópico pra ela revisar ainda hoje à noite.

---

## Seção 1 — Aquecimento (5 min, não pule — já é prática de verdade)

**P1.1 — "Me fale sobre você e sua experiência com automação de testes."**

> **Preste atenção em:** uma resposta ESTRUTURADA em menos de 2 minutos: cargo
> atual → principais ferramentas (deve mencionar Playwright com JavaScript no
> trabalho, e o framework em Selenium Java que construiu recentemente) → por que
> essa vaga na SAP. ❌ se ela enrolar além de 3 minutos ou só recitar o currículo
> sem conexão entre as partes.

**P1.2 — "Numa escala de 1 a 10, como você se avalia em Java? Justifique."**

> **Preste atenção em:** um número específico (não "depende"), seguido
> imediatamente de uma evidência — por exemplo, "um 6: me sinto confortável com
> conceitos de OOP e já construí um framework de testes usando eles, mas ainda não
> mexi muito com multithreading." Honestidade + evidência = ✅. Um número solto
> sem justificativa = 🟡.

---

## Seção 2 — Java e Programação Orientada a Objetos (15 min)

**P2.1 — "Quais são os quatro pilares da OOP? Me dê um exemplo real de cada um,
usando o próprio framework de testes que você construiu."**

> **Preste atenção em:** os quatro nomes — **encapsulamento (encapsulation),
> herança (inheritance), polimorfismo (polymorphism) e abstração (abstraction)**
> — cada um com um exemplo de teste, não um exemplo de livro didático. Bons
> sinais: "meus page objects escondem os locators como campos private"
> (encapsulamento); "minha LoginPage faz extends de BasePage e herda a
> configuração do driver" (herança); "o mesmo click() se comporta diferente no
> Chrome e no Firefox" (polimorfismo). ❌ se ela só listar os nomes sem exemplos.

**P2.2 — "Qual a diferença entre uma interface e uma classe abstrata (abstract
class)?"**

> **Preste atenção em:** a ideia de checklist-vs-esqueleto-parcial: uma interface
> é uma *lista de promessas sem nenhum código real*; uma classe abstrata *tem
> algum código pronto de verdade, mais algumas partes deixadas em branco de
> propósito*. Ponto extra se ela mencionar: uma classe pode fazer implements de
> VÁRIAS interfaces, mas só pode fazer extends de UMA classe; e que o próprio
> WebDriver do Selenium é uma interface, enquanto ChromeDriver é a classe que a
> implementa. Essa conexão com o Selenium é o sinal de ✅.

**P2.3 — "Para usar uma classe abstrata, você escreve extends ou implements? Por
quê?"**

> **Preste atenção em:** "**extends**" — na hora, sem hesitar — e a justificativa:
> uma classe abstrata continua sendo uma CLASSE; implements é reservado só para
> interfaces. Isso foi treinado recentemente; hesitação aqui = 🟡, revisar
> hoje à noite.

**P2.4 — "O que significam private, protected e public? E o que acontece se você
não escrever nenhum dos três?"**

> **Preste atenção em:** private = só a própria classe enxerga; protected = a
> classe mais suas subclasses; public = qualquer um. A pegadinha é a segunda
> parte: não escrever NADA é, na verdade, um quarto nível real, chamado
> **package-private / default** — só é visível dentro do mesmo pacote (package).
> Se ela acertar esse quarto nível sem você precisar puxar: ✅ forte.

**P2.5 — "O que é uma variável static? Por que o framework dela usou uma pra
guardar o WebDriver?"**

> **Preste atenção em:** static = pertence à CLASSE em si, uma cópia única
> compartilhada, em vez de cada objeto ter a sua própria. Conexão com o
> framework: a classe Hooks do Cucumber guarda um driver static para que as
> classes de step definitions consigam acessar a mesma instância do navegador.
> Ponto extra: mencionar que isso vira um problema em execuções PARALELAS (testes
> disputando um único driver) e citar ThreadLocal como a solução.

**P2.6 — "Qual a diferença entre == e .equals() para Strings em Java?"**

> **Preste atenção em:** `==` compara se as duas referências apontam para o MESMO
> objeto; `.equals()` compara o CONTEÚDO. Para comparar texto, quase sempre se
> quer `.equals()`. Resposta curta e segura = ✅.

---

## Seção 3 — Selenium (20 min — o coração da entrevista)

**P3.1 — "Explica a arquitetura do Selenium. O que acontece de verdade quando o
código diz driver.click()?"**

> **Preste atenção em:** a cadeia: código Java dela → biblioteca cliente do
> Selenium → uma requisição HTTP no protocolo **W3C WebDriver** → um
> **browser driver** (tipo o chromedriver) → o navegador de verdade. Ponto
> extra: o Selenium 4 removeu a camada de tradução antiga (JSON Wire Protocol)
> ao padronizar tudo em W3C. Uma resposta vaga tipo "é mágica" = ❌.

**P3.2 — "Que tipos de espera (wait) existem no Selenium, e qual ela usa?"**

> **Preste atenção em:** três tipos — **implicit** (um padrão global aplicado a
> toda busca de elemento), **explicit** (esperar por uma condição específica,
> tipo um elemento ficar clicável), **fluent** (explicit com frequência de
> verificação customizada). O ponto CHAVE que precisa aparecer: **nunca misturar
> implicit com explicit** — juntos, causam tempos de espera imprevisíveis. A
> preferência dela deve ser usar explicit waits em todo lugar. Não mencionar o
> "nunca misturar" = 🟡.

**P3.3 — "Como você decide qual locator usar para um elemento?"**

> **Preste atenção em:** uma ORDEM DE PRIORIDADE, não uma lista solta: id primeiro
> (se for estável), depois atributos específicos de teste tipo data-testid, depois
> name, depois CSS selectors, com XPath como último recurso e **XPath absoluto
> nunca**. Dois sinais extras de experiência real: (1) testar um locator no
> console do DevTools do navegador antes de usar, (2) verificar se um id não é
> gerado automaticamente, atualizando a página e inspecionando de novo.

**P3.4 — "Seu teste falha com StaleElementReferenceException. O que aconteceu e
como você resolve?"**

> **Preste atenção em:** a referência do elemento que ela tinha ficou inválida
> porque a página foi re-renderizada ou recarregada — o nó do DOM foi destruído
> e reconstruído. Solução: localizar o elemento de novo depois do que causou a
> mudança. Ponto extra grande se ela acrescentar: "essa exceção não existe no
> Playwright, porque os locators de lá pesquisam de novo toda vez que são
> usados" — essa percepção comparando as duas ferramentas é de nível sênior.

**P3.5 — "Como você automatizaria: um dropdown, um popup de alerta, e um
formulário dentro de um iframe?" (pergunte os três)**

> **Preste atenção em:** dropdown → a classe **Select**, mas SÓ para elementos
> `<select>` de verdade em HTML (dropdowns customizados com estilo próprio
> precisam de clique normal) — essa distinção é o sinal de experiência. Alert →
> `driver.switchTo().alert()` e depois accept/dismiss. Iframe → precisa **trocar
> para dentro do frame primeiro** (`switchTo().frame(...)`), agir, e depois
> `switchTo().defaultContent()` para sair. Não mencionar que precisa trocar
> primeiro no caso do iframe = 🟡.

**P3.6 — "O que é o Page Object Model e por que vale a pena usar?"**

> **Preste atenção em:** cada página ganha sua própria classe, guardando os
> locators e as ações daquela página; os testes chamam métodos e NUNCA tocam nos
> locators diretamente. O "por quê" que precisa aparecer: **quando a interface
> muda, você conserta UMA classe em vez de caçar em cada teste**. Ponto extra:
> ela construiu exatamente isso — LoginPage, CandidatePage, uma BasePage com
> configuração compartilhada — e consegue descrever.

**P3.7 — "O que o PageFactory.initElements faz?"**

> **Preste atenção em:** a ideia da caixa vazia com um bilhete colado (ou
> qualquer analogia equivalente): campos com @FindBy começam VAZIOS; initElements
> é o passo que conecta cada um a um buscador de elemento de verdade. Palavra
> extra: **"lazily" (de forma preguiçosa/tardia)** — a busca na página só
> acontece de verdade na primeira vez que cada campo é USADO, o que dá tempo
> para a página carregar.

---

## Seção 4 — TestNG e Design de Framework (10 min)

**P4.1 — "Me explica a ordem de execução das anotações do TestNG."**

> **Preste atenção em:** o aninhamento: BeforeSuite → BeforeTest → BeforeClass →
> BeforeMethod → **Test** → AfterMethod → AfterClass → AfterTest → AfterSuite.
> Não precisa decorar palavra por palavra, mas precisa ficar claro que
> Before/AfterMethod envolve CADA teste, enquanto Before/AfterClass roda só uma
> vez por classe.

**P4.2 — "Quando você usaria @BeforeMethod em vez de @BeforeClass para configurar
o navegador?"**

> **Preste atenção em:** @BeforeMethod (navegador novo a cada teste) como o
> PADRÃO SEGURO — total independência entre testes, sem estado vazando de um
> para o outro. @BeforeClass reservado para fluxos genuinamente SEQUENCIAIS, onde
> as etapas compartilham uma mesma sessão (ela construiu um fluxo de onboarding
> de candidato assim, usando dependsOnMethods para forçar a ordem). Se ela
> conseguir explicar essa troca — confiabilidade vs. velocidade — isso é ✅.

**P4.3 — "O que é um DataProvider e qual formato os dados dele têm?"**

> **Preste atenção em:** ele alimenta um método de teste com VÁRIOS conjuntos de
> entrada — o teste roda uma vez para cada linha de dado. O formato: **Object[][]**,
> uma grade onde a quantidade de valores de cada linha interna precisa bater
> EXATAMENTE com a quantidade de parâmetros do método de teste. Ponto extra se
> ela lembrar que uma linha com quantidade errada quebra em tempo de execução —
> ela já passou por esse bug pessoalmente.

**P4.4 — "Qual a diferença entre um hard assert e um soft assert?"**

> **Preste atenção em:** hard assert (Assert.assertEquals etc.) para o teste na
> hora, no primeiro erro; **SoftAssert junta todas as falhas** e mostra tudo
> junto quando você chama assertAll() — útil para verificar várias coisas numa
> mesma página, numa passada só.

**P4.5 — "Você também montou uma estrutura com Cucumber. O que fazem o feature
file, os step definitions e o runner?"**

> **Preste atenção em:** feature file = cenários em inglês simples, escritos em
> **Gherkin** (Given/When/Then), que pessoas não-técnicas conseguem ler; step
> definitions = os métodos Java conectados a cada linha em inglês; runner = a
> classe que dispara tudo. O insight CHAVE: os page objects dela **não
> precisaram de NENHUMA mudança** quando o Cucumber foi adicionado — prova de
> que as camadas estão bem separadas. Ponto extra: os hooks @Before/@After do
> Cucumber são DIFERENTES dos @BeforeMethod do TestNG — um bug que ela mesma
> encontrou e corrigiu.

---

## Seção 5 — Playwright e Comparação de Ferramentas (10 min)

**P5.1 — "Você usa Playwright no dia a dia. Compara com Selenium — arquitetura,
espera, e confiabilidade."**

> **Preste atenção em:** três contrastes: (1) **transporte** — Selenium manda
> cada comando como uma requisição HTTP separada, através de um programa driver;
> Playwright mantém UMA conexão aberta direto com o navegador, sem intermediário;
> (2) **espera** — Playwright espera automaticamente antes de cada ação (elemento
> visível, estável, habilitado, não coberto), enquanto no Selenium as esperas são
> escritas manualmente; (3) **elementos obsoletos** — os locators do Playwright
> são "receitas" que rodam de novo a cada uso, então elemento obsoleto não pode
> nem existir. Um fechamento equilibrado — "o ponto forte do Selenium é a
> maturidade e a presença enorme em empresas grandes, que é exatamente por que a
> SAP ainda roda Selenium" — é o sinal de ✅. Fanatismo por uma ferramenta
> ("Selenium tá obsoleto") = 🟡.

**P5.2 — "O que é um BrowserContext e por que ele deixa o Playwright rápido em
execução paralela?"**

> **Preste atenção em:** uma sessão isolada DENTRO de um navegador — tipo uma
> janela anônima/incógnita; com seus próprios cookies e logins; quase de graça
> para criar. A matemática da paralelização: Selenium isola por navegador
> inteiro (pesado), Playwright isola por context (barato) — então 10 testes em
> paralelo não significam 10 Chromes abertos.

**P5.3 — "Por que o Playwright recomenda getByRole em vez de seletores CSS?"**

> **Preste atenção em:** roles consultam a **árvore de acessibilidade** — a
> página como um USUÁRIO a percebe (um botão chamado Submit) em vez de como ela
> foi implementada (um nome de classe CSS). Resultado: o teste só quebra quando a
> experiência do usuário realmente quebra, não quando um desenvolvedor renomeia
> uma classe de estilo.

**P5.4 — "Um teste no Playwright está instável (flaky). Me explica como você
depuraria isso."**

> **Preste atenção em:** rodar com **trace on** → abrir o **trace viewer** →
> inspecionar o snapshot do DOM no passo que falhou, as chamadas de rede, o
> console. Depois, os culpados típicos: uma corrida (race condition) que o
> auto-wait não enxerga, testes que dependem uns dos outros, ou um locator
> frágil, ligado demais à implementação.

---

## Seção 6 — SQL (10 min — estudado recentemente, confirme se ficou)

*Para esta seção, diga a ela: "Imagina uma tabela Customers e uma tabela Orders,
onde os pedidos apontam para os clientes através do id do cliente. Alguns
clientes nunca fizeram pedido."*

**P6.1 — "O que é uma primary key e uma foreign key, e por que precisamos da
foreign key se a primary key já existe?"**

> **Preste atenção em:** primary key = o identificador único de cada linha
> DENTRO DA PRÓPRIA tabela (tipo um CPF). Foreign key = uma coluna que APONTA
> para a primary key de outra tabela. O "por que precisa das duas": a primary
> key só garante que os valores são únicos dentro da própria tabela — a foreign
> key é o que impede referências inválidas (um pedido apontando para o cliente
> 999, que não existe). **Duas chaves, dois trabalhos diferentes** — essa
> forma de explicar é o ✅.

**P6.2 — "Explica INNER JOIN vs LEFT JOIN. Quais clientes desaparecem num
resultado de INNER JOIN, e por que alguém ia querer eles de volta?"**

> **Preste atenção em:** INNER JOIN mantém SÓ as linhas que dão match nos dois
> lados — clientes com zero pedidos DESAPARECEM. LEFT JOIN mantém TODA linha da
> tabela da esquerda, preenchendo o lado que falta com **NULL**. O "por que
> alguém ia querer os NULLs de volta": às vezes os "sem match" SÃO a resposta —
> "quais clientes nunca fizeram pedido" só é respondível com LEFT JOIN mais
> `WHERE ... IS NULL`. Se ela comentar sozinha que isso também é um cenário de
> QA (testar como o sistema se comporta com clientes de zero pedidos): ✅ forte.

**P6.3 — "O que é um índice (index), e qual a troca envolvida (trade-off)?"**

> **Preste atenção em:** a comparação com o índice de um livro — pular direto
> para a página certa em vez de ler página por página. A troca PRECISA aparecer:
> leituras naquela coluna ficam mais rápidas, mas cada inserção/atualização fica
> um pouco mais lenta, porque o índice também precisa ser atualizado. Resposta
> sem mencionar a troca = 🟡.

**P6.4 — "O que é um trigger? Dá um exemplo de uso."**

> **Preste atenção em:** uma ação automática e armazenada, disparada quando algo
> muda numa tabela — o clássico: ao apagar a linha de um funcionário, copiar
> essa linha automaticamente para uma tabela de auditoria antes, sem que
> ninguém precise lembrar de fazer isso manualmente.

**P6.5 — "Fala em voz alta, de forma aproximada, o SQL para: total gasto por
cliente, incluindo os clientes que não gastaram nada."**

> **Preste atenção em (essa é a mais difícil):** três ingredientes precisam
> aparecer juntos — **LEFT JOIN** (para os clientes de zero pedido
> sobreviverem), **SUM(amount)**, e **GROUP BY** pelo cliente. A sintaxe exata
> não precisa estar perfeita; os três ingredientes é que contam. Os três juntos
> = ✅ forte. Só INNER JOIN, ou faltando o GROUP BY = 🟡, revisar hoje à noite.

---

## Seção 7 — Puzzles (10 min — avalie a NARRAÇÃO, não a resposta)

*Regras para você: não dê nenhuma dica por 2 minutos. O que você está avaliando
é se ela pensa EM VOZ ALTA. Silêncio é o erro principal aqui, não estar errado.*

**P7.1 — "25 máquinas produzem pesos de 1kg. Uma máquina com defeito produz
pesos de 999g. Você tem uma balança de precisão que só pode usar UMA vez. Ache
a máquina com defeito."**

> **Preste atenção em:** o truque é pegar uma QUANTIDADE DIFERENTE de cada
> máquina — 1 peso da máquina 1, 2 pesos da máquina 2, ... 25 pesos da máquina
> 25 — e depois pesar a pilha inteira de uma vez só. Os gramas que FALTAM do
> total esperado indicam exatamente o número da máquina com defeito (faltando
> 17g = máquina 17). ✅ se aparecer a ideia de "quantidades diferentes fazem
> cada suspeito deixar uma marca única". OURO se ela disser "deixa eu tentar
> uma versão menor primeiro, com 3 máquinas" — esse instinto é a habilidade
> real sendo testada.

**P7.2 — "10 bolinhas idênticas, uma mais pesada. Uma balança de dois pratos
(sem números). Quantas pesagens no mínimo para garantir achar a bolinha mais
pesada?"**

> **Preste atenção em:** o insight chave PRIMEIRO: uma pesagem tem TRÊS
> resultados possíveis (prato esquerdo desce, prato direito desce, ou empata) —
> então o certo é dividir em terços, não em metades, deixando um grupo de fora
> da balança. Depois: 9 bolinhas se resolvem em 2 pesagens (9→3→1), e a décima
> bolinha estoura essa conta, forçando **3 pesagens no pior caso**. O raciocínio
> falado em voz alta importa muito mais do que o número final.

**P7.3 (só se sobrar tempo) — invente uma pergunta de estimativa qualquer, tipo
"quantos cafés uma cafeteria num bairro movimentado vende por dia?"**

> **Preste atenção em:** decomposição estruturada falada em voz alta — horário
> de funcionamento × clientes por hora × horários de pico vs. horários fracos —
> com premissas ditas claramente. QUALQUER número bem justificado é aprovado; um
> chute solto sem mostrar o raciocínio é o único jeito de reprovar aqui.

---

## Seção 8 — API, Git, Testes Manuais (8 min, perguntas rápidas)

**P8.1 — "O que é teste de API e por que os times querem MUITOS testes de API e
POUCOS testes de interface (UI)?"**

> **Preste atenção em:** testar a comunicação direta entre programas — enviar
> requisições, verificar respostas, sem navegador. O "por quê": testes de API
> são muito mais rápidos e menos instáveis (sem carregamento de página, sem
> locators), então o formato saudável é muitos testes de API, com automação de
> UI reservada para jornadas reais do usuário. Mencionar **Postman** (manual) e
> **REST Assured** (automação em Java, se encaixa no TestNG do mesmo jeito que
> o Selenium) = ✅.

**P8.2 — "Uma requisição retorna 401. Outra retorna 500. De quem é a culpa em
cada caso?"**

> **Preste atenção em:** 401 = culpa de QUEM CHAMOU (não está autenticado — a
> família 4xx é "erro meu"); 500 = culpa do SERVIDOR (a família 5xx é "erro
> deles"). Resposta rápida e segura = ✅.

**P8.3 — "git pull vs git fetch — resposta direta e clara."**

> **Preste atenção em:** fetch baixa os commits novos SEM tocar no código de
> trabalho dela (dá pra olhar antes de aplicar); pull = fetch + merge, tudo de
> uma vez. "Fetch é o passo cauteloso em duas etapas, pull é o atalho."

**P8.4 — "Smoke, sanity, regression e re-testing — defina rapidamente cada um."**

> **Preste atenção em:** smoke = verificação rápida de que os caminhos críticos
> de uma build funcionam, no mínimo; sanity = verificação estreita de uma
> correção específica; regression = rodar os testes já existentes de novo para
> confirmar que nada ANTIGO quebrou; re-testing = rodar de novo exatamente o
> teste que encontrou um bug específico, para confirmar que AQUELE bug foi
> corrigido. A distinção entre regression e re-testing é a que as pessoas mais
> confundem — preste atenção especial aí.

**P8.5 — "Me explica o ciclo de vida de um defeito (defect life cycle)."**

> **Preste atenção em:** New → Assigned → Open → Fixed → Retest → Verified →
> Closed, com o retorno: se falhar no reteste → **Reopened**. Citar esse
> caminho de volta (reopen) = ✅.

---

## Seção 9 — Perguntas Gerenciais e de RH (10 min — aqui você é forte de
verdade; seja um gestor de verdade)

**P9.1 — "Por que a SAP?"**

> **Preste atenção em:** algo ESPECÍFICO — os produtos deles, a escala da área
> de qualidade lá, o domínio de negócio — não elogio genérico. Questione uma
> vez: "isso poderia descrever qualquer empresa grande. Por que a SAP
> especificamente?"

**P9.2 — "Me conta sobre uma tarefa que não foi concluída no prazo, e você
precisou tomar decisões rápidas."**

> **Preste atenção em:** UMA história concreta e verdadeira, com essa forma:
> situação → a decisão tomada → resultado → o que ela repetiria ou mudaria.
> Generalidades vagas ("eu sempre me comunico cedo") sem uma história real por
> trás = 🟡.

**P9.3 — "E se a gente oferecesse a você uma posição de tester em vez de
developer?"**

> **Preste atenção em (pergunta feita de verdade numa entrevista real da SAP):**
> uma posição já decidida e genuína — uma resposta forte abraça o híbrido:
> "eu escrevo código, e o propósito do meu código é garantir qualidade — é isso
> que Quality Engineer significa pra mim." Hesitação visível ou contradição =
> ela ainda não decidiu; faça ela decidir isso ainda hoje à noite.

**P9.4 — "Qual é um ponto fraco real seu?"**

> **Preste atenção em:** um ponto fraco DE VERDADE, junto com a ação que ela
> toma para lidar com ele. Reprovação automática: "eu sou perfeccionista" ou
> qualquer "defeito" que na verdade é um elogio disfarçado.

**P9.5 — Termine com: "Você tem alguma pergunta pra mim?"**

> **Preste atenção em:** ela precisa ter DUAS perguntas preparadas. Boas
> perguntas: como o time de QE divide trabalho manual e automação; como é o
> dia a dia da responsabilidade sobre qualidade no time. Não ter nenhuma
> pergunta é reprovação também numa entrevista real — trate como ❌ aqui
> também.

---

## Depois da entrevista — 10 minutos de retrospectiva, juntos

1. Releiam juntos cada 🟡 e ❌, com o número da seção — essa é a lista de
   revisão de hoje à noite, já priorizada.
2. Diga a ela o momento em que ela pareceu MAIS segura, e o momento em que
   pareceu MENOS segura. (Ela não consegue perceber isso de dentro da própria
   cabeça.)
3. Uma checagem de honestidade: em algum momento ela blefou em vez de dizer
   "não sei, mas aqui está como eu pensaria sobre isso"? Blefar soa pior do que
   incerteza honesta numa entrevista real — sinalize se você percebeu isso.
4. Se ela ficou em silêncio nos puzzles: repita UM puzzle no final, com a única
   regra sendo "narre todo pensamento, mesmo os ruins". O hábito, não a
   resposta, é o objetivo desse treino.
