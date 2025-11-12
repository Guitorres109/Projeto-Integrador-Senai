function togglePassword() {
      const senha = document.getElementById("senha");
      senha.type = senha.type === "password" ? "text" : "password";
      document.getElementById("botaosenha").classList.toggle("ativo");
    }

    // Exibir alerta personalizado
    function showAlert(mensagem) {
      const alertBox = document.getElementById('customAlert');
      const alertMessage = document.getElementById('alertMessage');
      alertMessage.textContent = mensagem;
      alertBox.style.display = 'block';
    }

    // Esconder alerta
    function hideAlert() {
      document.getElementById('customAlert').style.display = 'none';
    }

    // Atualizar exibição com base na query string
    function atualizarExibicao() {
      const params = new URLSearchParams(window.location.search);
      document.getElementById('usuario').value = params.get('usuario') || '';
      document.getElementById('senha').value = params.get('senha') || '';
      document.getElementById('tipo').value = params.get('tipo') || '';
    }

    // Enviar dados e atualizar a URL
    function enviarDados() {
      const usuario = document.getElementById('usuario').value.trim();
      const senha = document.getElementById('senha').value;
      const tipo = document.getElementById('tipo').value;
      const respostaDiv = document.getElementById('resposta');

      if (!usuario || !senha) {
        showAlert('Por favor, preencha o nome e a senha.');
        return;
      }

      if (!tipo) {
        showAlert('Por favor, selecione um perfil de usuário.');
        return;
      }

      // Atualiza a URL com os parâmetros
      const url = new URL(window.location.href);
      url.searchParams.set('usuario', usuario);
      url.searchParams.set('senha', senha);
      url.searchParams.set('tipo', tipo);
      window.history.pushState({}, '', url);

      respostaDiv.textContent = "Dados enviados com sucesso!";
      atualizarExibicao();
    }

    // Atualiza a exibição ao carregar a página
    window.onload = atualizarExibicao;
    
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