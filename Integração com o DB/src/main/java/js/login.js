var usuarios = [
  "Guilherme Torres", "Nicolas Tordino", "João Pedro", "Pietro Pardim", 
  "Mariana Adel Ayoub", "Giovanna Alves de Almeida", "Ana Carolina Santos Denobi",
  "Felipe Chagas Machado", "Fernanda Cristina Rodrigues Ferreira",
  "Carina Cristina Teixeira de Souza", "Henrique da Silva Lima",
  "Eduardo de Figueiredo Ferreira Gandra", "Rayka Dom Pedro Hirata",
  "Henrique Duarte de Sousa", "Maria Eduarda Silva Freitas",
  "Raphaela Felix de Araujo", "Lucas Gabriel de Moura Adorni", 
  "André Gustavo Pavanelli", "Ana Julia Lima dos Santos", 
  "Sarah Kohn Baldoini", "Matheus Lopes Ferreira", 
  "Yasmin Lopes Souza", "Arthur Marques Duarte Silva", 
  "Gustavo Nunes da Silva", "Isabelle Queiroz Rodrigues", 
  "Felipe Santos Silva", "Otávio Silva Garbin", 
  "Isabelly Sofia Domingues", 
  "Kauã Teles Santos", "Luiza Timporini Frazão", 
  "Gabriel Viana dos Reis", "João Vitor Ulisses Bacurau", "Eduardo Fallabela"
];

// Mapear senhas para cada usuário
var senhas = {
  "Guilherme Torres": "12345678",
  "Nicolas Tordino": "neymarjunior",
  "João Pedro": "horadopaupreto",
  "Pietro Pardim": "naofechocomx9",
  // Adicionando as novas senhas (você pode personalizar essas senhas)
  "Mariana Adel Ayoub": "12345678", //1
  "Giovanna Alves de Almeida": "12345678", //2
  "Ana Carolina Santos Denobi": "12345678", //3
  "Felipe Chagas Machado": "senhaFelipe", //4
  "Fernanda Cristina Rodrigues Ferreira": "12345678", //5
  "Carina Cristina Teixeira de Souza": "12345678", //6
  "Henrique da Silva Lima": "12345678", //7
  "Eduardo de Figueiredo Ferreira Gandra": "12345678", //8
  "Rayka Dom Pedro Hirata": "12345678", //9
  "Henrique Duarte de Sousa": "12345678", //10
  "Maria Eduarda Silva Freitas": "12345678", //11
  "Raphaela Felix de Araujo": "12345678", //12
  "Lucas Gabriel de Moura Adorni": "12345678", //13
  "André Gustavo Pavanelli": "12345678", //14
  "Ana Julia Lima dos Santos": "12345678", //15
  "Sarah Kohn Baldoini": "12345678", //16
  "Matheus Lopes Ferreira": "12345678", //17
  "Yasmin Lopes Souza": "12345678", //18
  "Arthur Marques Duarte Silva": "12345678", //19
  "Gustavo Nunes da Silva": "12345678", //20
  "Isabelle Queiroz Rodrigues": "12345678", //21
  "Felipe Santos Silva": "12345678", //22
  "Otávio Silva Garbin": "12345678", //23
  "Isabelly Sofia Domingues": "12345678", //24
  "Kauã Teles Santos": "12345678", //25
  "Luiza Timporini Frazão": "12345678", //26
  "Gabriel Viana dos Reis": "12345678", //27
  "João Vitor Ulisses Bacurau": "12345678", //28
  "Eduardo Fallabela": "12345678"
};

// Mapear o tipo de usuário para cada nome
var tipos_usuario = {
  "Guilherme Torres": "professor",
  "Nicolas Tordino": "professor",
  "João Pedro": "professor",
  "Pietro Pardim": "professor",
  // Definindo como 'aluno' para todos os novos usuários
  "Mariana Adel Ayoub": "aluno",
  "Giovanna Alves de Almeida": "aluno",
  "Ana Carolina Santos Denobi": "aluno",
  "Felipe Chagas Machado": "aluno",
  "Fernanda Cristina Rodrigues Ferreira": "aluno",
  "Carina Cristina Teixeira de Souza": "aluno",
  "Henrique da Silva Lima": "aluno",
  "Eduardo de Figueiredo Ferreira Gandra": "aluno",
  "Rayka Dom Pedro Hirata": "aluno",
  "Henrique Duarte de Sousa": "aluno",
  "Maria Eduarda Silva Freitas": "aluno",
  "Raphaela Felix de Araujo": "aluno",
  "Lucas Gabriel de Moura Adorni": "aluno",
  "André Gustavo Pavanelli": "aluno",
  "Ana Julia Lima dos Santos": "aluno",
  "Sarah Kohn Baldoini": "aluno",
  "Matheus Lopes Ferreira": "aluno",
  "Yasmin Lopes Souza": "aluno",
  "Arthur Marques Duarte Silva": "aluno",
  "Gustavo Nunes da Silva": "aluno",
  "Isabelle Queiroz Rodrigues": "aluno",
  "Felipe Santos Silva": "aluno",
  "Otávio Silva Garbin": "aluno",
  "Isabelly Sofia Domingues": "aluno",
  "Kauã Teles Santos": "aluno",
  "Luiza Timporini Frazão": "aluno",
  "Gabriel Viana dos Reis": "aluno",
  "João Vitor Ulisses Bacurau": "aluno",
  "Eduardo Fallabela": "professor"
};


var input_usuario = document.getElementById('usuario'); // O campo para o nome do usuário
var input_senha = document.getElementById('senha'); // O campo para a senha
var input_tipo = document.getElementById('tipo');

function verificar_usuario() {
    var usuario_digitado = input_usuario.value;
    var senha_digitada = input_senha.value;
    var tipo_digitado = input_tipo.value;

    // Verifica se o usuário existe
    var usuario_existe = usuarios.includes(usuario_digitado);

    // Salva o status de usuário_existe no localStorage
    localStorage.setItem("usuario_existe", usuario_existe);

    // Verifica se a senha corresponde ao usuário
    var senha_existe = senhas[usuario_digitado] === senha_digitada;

    if (usuario_existe && senha_existe) {
        // Login correto, vamos verificar o tipo de usuário
        var tipo_usuario = tipos_usuario[usuario_digitado];
        if (tipo_usuario === tipo_digitado){
          if (tipo_usuario === "professor") {
              alert("Bem-vindo, Professor " + usuario_digitado + "!");
              // Redireciona para a página do professor
              window.location.href = "/acesso-professor.html"; // Página para professores
          } else if (tipo_usuario === "aluno") {
              alert("Bem-vindo, Aluno " + usuario_digitado + "!");
              // Redireciona para a página do aluno
              window.location.href = "/aluno"; // Página para alunos
          }
        } else {
          alert("Usuário não encontrado! Tente Novamente.");
        }

        localStorage.setItem("usuario", usuario_digitado);
    } else {
        alert("Usuário ou senha incorretos! Tente novamente.");
    }
}



function togglePassword() {
  const senhaInput = document.getElementById("senha");
  senhaInput.type = senhaInput.type === "password" ? "text" : "password";
  document.getElementById("botaosenha").classList.toggle("ativo");
}
function toggleTheme() {
    document.body.classList.toggle("dark-mode");

    // salvar preferência
    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("tema", "escuro");
    } else {
        localStorage.setItem("tema", "claro");
    }
    const darkModeButton = document.querySelector('.dark-mode-button');
}

// botão
document.getElementById("toggle-theme").addEventListener("click", toggleTheme);

// carregar preferencia ao abrir a página
if (localStorage.getItem("tema") === "escuro") {
    document.body.classList.add("dark-mode");
}

// Função para esconder o alerta
function hideAlert() {
    document.getElementById('customAlert').style.display = 'none';
}
const items = document.querySelectorAll('.reveal');

const observer = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
        entry.target.classList.add('active');
        observer.unobserve(entry.target); // só uma vez
    }
  });
}, { threshold: 0.5 });

items.forEach(item => observer.observe(item));