var usuarios = ["Guilherme Torres", "Nicolas Tordino", "João Pedro", "Pietro Pardim"];

// Mapear senhas para cada usuário
var senhas = {
  "Guilherme Torres": "12345678",
  "Nicolas Tordino": "neymarjunior",
  "João Pedro": "horadopaupreto",
  "Pietro Pardim": "naofechocomx9"
};

// Mapear o tipo de usuário para cada nome
var tipos_usuario = {
  "Guilherme Torres": "professor",
  "Nicolas Tordino": "professor",
  "João Pedro": "professor",
  "Pietro Pardim": "professor"
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

    // Verifica se a senha corresponde ao usuário
    var senha_existe = senhas[usuario_digitado] === senha_digitada;

    if (usuario_existe && senha_existe) {
        // Login correto, vamos verificar o tipo de usuário
        var tipo_usuario = tipos_usuario[usuario_digitado];
        if (tipo_usuario === tipo_digitado){
          if (tipo_usuario === "professor") {
              alert("Bem-vindo, Professor " + usuario_digitado + "!");
              // Redireciona para a página do professor
              window.location.href = "acesso-professor.html"; // Página para professores
          } else if (tipo_usuario === "aluno") {
              alert("Bem-vindo, Aluno " + usuario_digitado + "!");
              // Redireciona para a página do aluno
              window.location.href = "Atividades.html"; // Página para alunos
          }
        } // Acessa o tipo de usuário com base no nome
        else{
          alert("Usuário não encontrado! Tente Novamente.");
        }

        localStorage.setItem("usuario",usuario_digitado);
        console.log(localStorage);
    } else {
        // Login incorreto, impede o avanço
        // document.getElementById('customAlert').style.display = 'block'; // Exibe o alerta personalizado
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