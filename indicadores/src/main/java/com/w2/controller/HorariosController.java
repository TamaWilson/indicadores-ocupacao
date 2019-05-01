package com.w2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.w2.model.Centro;
import com.w2.model.Horario;
import com.w2.model.HorarioPadrao;
import com.w2.model.Predio;
import com.w2.model.Sala;
import com.w2.model.Semestre;
import com.w2.model.support.Curso;
import com.w2.model.support.Disciplina;
import com.w2.model.util.DiasSemana;
import com.w2.model.util.HorararioOrdem;
import com.w2.model.util.Turnos;
import com.w2.repository.CentrosRepositorio;
import com.w2.repository.HorarioRepositorio;
import com.w2.repository.HorariosPadraoRepositorio;
import com.w2.repository.PrediosRepositorio;
import com.w2.repository.SalasRepositorio;
import com.w2.repository.SemestreRepositorio;
import com.w2.service.JsonService;


@Controller
@SessionAttributes(value = {"horarioRegistroNovo", "horariosConflitantes", "horariosCadastrados", "horariosRemove", "jsonResultado"})
@RequestMapping(value="cadhorarios") 
public class HorariosController {



	@Autowired
	private CentrosRepositorio centros;

	@Autowired
	private PrediosRepositorio predios;

	@Autowired
	private SalasRepositorio salas;

	@Autowired
	private HorariosPadraoRepositorio horarioPadrao;

	@Autowired
	private SemestreRepositorio semestres;

	@Autowired
	private HorarioRepositorio horarios;

	//MONTA A VIEW COM UM SELECT DE CURSOS DE UM CENTRO - O RESTO VAZIO
	@RequestMapping
	public ModelAndView cadastrarHorario(@ModelAttribute("sigla") String siglaCentro, @ModelAttribute("predio") String idPredio) { 

		ModelAndView modelView = new ModelAndView("FiltrarDisciplina");

		Predio predioEscolhido = new Predio();
		List<Sala> todasSalas = new ArrayList<Sala>();

		if(idPredio == null || idPredio.equals(""))		{ 
			predioEscolhido.setIdPredio(0);


		}
		else 
		{ 
			predioEscolhido = predios.findByIdPredioAndSemestre(Integer.parseInt(idPredio), semestreAtual());

			todasSalas = salas.findSalaByPredioOrderByNumeroSalaAsc(predioEscolhido);

		}


		modelView.addObject("salas", todasSalas);


		Centro centroAtual = centros.findCentroBySiglaLike(siglaCentro);

		List<Curso> cursosLista = new ArrayList<Curso>();
		JsonService jsonCurso = new JsonService();
		JsonNode  disciplinasJson =  jsonCurso.getCentroJson(siglaCentro); 
		JsonNode  cursoJson = disciplinasJson;

		ArrayNode cursosNodes =  (ArrayNode) cursoJson.at("/data/cursos");

		for(JsonNode cursoNode : cursosNodes ) { 
			Curso curso = new Curso();
			curso.setCurso_nome(cursoNode.at("/curso_nome").asText());
			curso.setCurso_id(cursoNode.at("/curso_id").intValue());
			curso.setCurso_codigo(cursoNode.at("/curso_codigo").asText());

			cursosLista.add(curso);
		}


		List<Predio> prediosLista = predios.findPredioBySemestreAndCentro_SiglaLike(semestreAtual(), siglaCentro);

		modelView.addObject("centro", centroAtual);
		modelView.addObject("predios", prediosLista);
		modelView.addObject("cursos", cursosLista);
		modelView.addObject("predioEscolhido", predioEscolhido);

		return modelView;
	}


	//VIA AJAX RECUPERA OS PERIODOS E CRIA UM JSONRESULSTADO PARA DIMINUI A CARGA EM CIMA DO WS
	@RequestMapping(value="/periodos", method=RequestMethod.POST)
	public String listarPeriodos(Model model, @RequestParam("cursoSelecionado") int cursoSelecionado) {

		Set<Integer> periodosDisponiveis = new HashSet<Integer>();

		JsonService jsonCurso = new JsonService();
		JsonNode jsonResultado =  jsonCurso.getCursoJson(cursoSelecionado);
		JsonNode disciplinasJson = jsonResultado;
		ArrayNode disciplinasNodes =  (ArrayNode) disciplinasJson.at("/data/periodos");
		for(JsonNode disciplinaNode : disciplinasNodes ) { 
			Disciplina disciplina = new Disciplina();
			disciplina.setDisciplinaPeriodo(disciplinaNode.at("/turma_periodo").asInt());
			periodosDisponiveis.add(disciplina.getDisciplinaPeriodo());
		}



		model.addAttribute("jsonResultado", jsonResultado);
		model.addAttribute("periodos", periodosDisponiveis);
		return "PeriodosSeletor";				
	}

	//VIA AJAX RECEBE O PERIODO SELECIONADO E ENVIA UMA LISTA DE DISCIPLINAS DAQUELE PERIODO PARA A VIEW
	@RequestMapping(value="/disciplinas", method=RequestMethod.POST)
	public String listarDisciplinas(Model model, @RequestParam("periodoSelecionado") int periodoSelecionado, @ModelAttribute("jsonResultado") JsonNode  jsonResultado) {


		List<Disciplina> disciplinasLista = new ArrayList<Disciplina>();

		JsonNode disciplinasJson = jsonResultado;
		ArrayNode disciplinasNodes =  (ArrayNode) disciplinasJson.at("/data/periodos");
		for(JsonNode disciplinaNode : disciplinasNodes ) { 

			int peridoFiltro = disciplinaNode.at("/turma_periodo").asInt();
			if(peridoFiltro == periodoSelecionado) {
				Disciplina disciplina = new Disciplina();

				disciplina.setDisciplinaPeriodo(peridoFiltro);
				disciplina.setDisciplinaNome(disciplinaNode.at("/disciplinas").get(0).at("/disciplina_nome").asText());
				disciplina.setDisciplinaId(disciplinaNode.at("/disciplinas").get(0).at("/disciplina_id").asInt());
				disciplinasLista.add(disciplina);
			}
		}

		model.addAttribute("disciplinas", disciplinasLista);
		return "DisciplinasSeletor";

	}


	//VIA AJAX RECEBE O CENTRO SELECIONA E MONTA LISTA DE PREDIOS PARA USAR NO SELECT
	@RequestMapping(value="/predios", method=RequestMethod.POST)
	public String listarSalas(Model model, @RequestParam("centroSelecionado") String centroSigla)
	{

		List<Predio> todosPredios = predios.findPredioBySemestreAndCentro_SiglaLike(semestreAtual(), centroSigla);
		model.addAttribute("predios", todosPredios);

		return "PredioSeletor";

	}


	//VIA AJAX RECEBR O PREDIO SELECIONADO E MONTA A LISTA DE SALAS PARA USAR NO SELECT
	@RequestMapping(value="/salas", method=RequestMethod.POST)
	public String listarSalas(Model model, @RequestParam("predioSelecionado") Predio predioSelecionado) {


		List<Sala> todasSalas = salas.findSalaByPredioOrderByNumeroSalaAsc(predioSelecionado);
		model.addAttribute("salas", todasSalas);

		return "SalasSeletor";		
	}


	//RECEBE 
	@RequestMapping(value="/horarios", method=RequestMethod.POST)
	public ModelAndView organizarHorarios(Disciplina disciplina, @RequestParam("sala") Sala sala, RedirectAttributes redirectAttributes,@ModelAttribute("jsonResultado") JsonNode  jsonResultado) { 

		List<Horario> disciplinaRegistrada = horarios.findByDisciplinaIdAndSemestre(disciplina.getDisciplinaId(), semestreAtual());

		List<Horario> horariosCadastrados = horarios.findBySalaAndSemestre(sala, semestreAtual());

		List<Horario> horariosLista = new ArrayList<Horario>();

		List<Horario> horariosConflitantes = new ArrayList<Horario>();		
		
		List<Horario> horariosRemove = new ArrayList<Horario>();		
		
		if(disciplinaRegistrada.size() == 0){

			//--- Listando os novos horários
			Configuration conf = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
					.options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();

			ReadContext contexto = JsonPath.using(conf).parse(jsonResultado.toString());

			ArrayNode disciplinaNome = contexto.read("$.data.periodos..disciplinas[?(@.disciplina_id == " + disciplina.getDisciplinaId() + ")].disciplina_nome");

			ArrayNode professorNome = contexto.read("$.data.periodos..disciplinas[?(@.disciplina_id == " + disciplina.getDisciplinaId() + ")].professor_nome");

			ArrayNode disciplinaCodigo = contexto.read("$.data.periodos..disciplinas[?(@.disciplina_id == " + disciplina.getDisciplinaId() + ")].disciplina_codigo");

			ArrayNode disciplinaCursoId = contexto.read("$.data.curso_id");

			ArrayNode horariosArray = contexto.read("$.data.periodos..disciplinas[?(@.disciplina_id == " + disciplina.getDisciplinaId() + ")].horarios[*]");


			for(JsonNode horarioDetalhes : horariosArray) {

				Horario horario = new Horario();

				horario.setDisciplinaNome(disciplinaNome.get(0).asText());
				horario.setDisciplinaCodigo(disciplinaCodigo.get(0).asText());
				horario.setProfessorNome(professorNome.get(0).asText());

				int tipo = horarioDetalhes.at("/horario_tipo").asInt();
				int ordem = horarioDetalhes.at("/horario_ordem").asInt();
				int diaSemana = horarioDetalhes.at("/horario_dia_semana").asInt();

				horario.setTurmaPeriodo(disciplina.getDisciplinaPeriodo());


				horario.setTipoDescricao(tipo);
				horario.setHorarioOrdem(ordem);
				horario.setDiaSemana(diaSemana);

				horario.setIdCurso(disciplinaCursoId.get(0).asInt());
				horario.setDisciplinaId(disciplina.getDisciplinaId());
				horario.setSemestre(semestreAtual());

				HorarioPadrao horarioPadraoAtual = horarioPadrao.findByOrdemAndTipoAndSemestre(ordem, tipo, semestreAtual());
				horario.setHorarioPadrao(horarioPadraoAtual);

				horario.setSala(sala);


				Boolean conflito = false;

				for(Horario horarioConflito : horariosCadastrados) { 
					if(horarioConflito.getDiaSemana() == diaSemana & horarioConflito.getHorarioOrdem() == ordem  & horarioConflito.getTipoDescricao() == tipo) { 


						HashMap<String, String> mensagens = new HashMap<>();
						mensagens.put("classe", "text-danger glyphicon glyphicon-remove-sign");
						mensagens.put("status", "Disciplina não alocada!");

						horario.setStatus(mensagens);

						horariosConflitantes.add(horario);
						conflito = true;
						break;
					}
				}

				if(!conflito) { 
					horariosLista.add(horario);
				}

			}
			//---------------------------------------------//

		}

		if(horariosLista.size() == 0  & horariosCadastrados.size() == 0 & horariosConflitantes.size() == 0) {
			ModelAndView modelView = new ModelAndView("redirect:/cadhorarios");

			redirectAttributes.addFlashAttribute("sigla", sala.getPredio().getCentro().getSigla());
			return 	modelView;
		} else {
			
			System.out.println(">>>>>>>>>>>>" + horariosRemove.size());

			
			
			ModelAndView modelView = new ModelAndView("DistribuirHorarios");

			modelView.addObject("horarioRegistroNovo", horariosLista);
			modelView.addObject("horariosCadastrados", horariosCadastrados);
			modelView.addObject("horariosConflitantes", horariosConflitantes);
			modelView.addObject("horariosRemove", horariosRemove);

			
			List<Centro> centrosLista = centros.findAll();
			modelView.addObject("centrosLista", centrosLista);

			//Informações estáticas
			modelView.addObject("sala", sala);

			modelView.addObject("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual()));

			return modelView;

		}
	}

	@RequestMapping(value="/horarios/salvar", method = RequestMethod.POST)
	public ModelAndView salvarHorarios( @ModelAttribute("horariosRemove")  List<Horario> horariosRemove, @ModelAttribute("horarioRegistroNovo") List<Horario> horarioLista, @ModelAttribute("horariosConflitantes") List<Horario> horariosConflitantes){


		
		horarios.delete(horariosRemove);
		
		List<Horario> horarioNovoSemConflito = new ArrayList<Horario>();
		for(Horario horario : horarioLista){ 

			Horario horarioNovo = new Horario();

			horarioNovo.setIdCurso(horario.getIdCurso());
			horarioNovo.setDisciplinaId(horario.getDisciplinaId());

			horarioNovo.setTurmaPeriodo(horario.getTurmaPeriodo());

			horarioNovo.setTipoDescricao(horario.getTipoDescricao());
			horarioNovo.setHorarioOrdem(horario.getHorarioOrdem());
			horarioNovo.setDiaSemana(horario.getDiaSemana());
			horarioNovo.setDisciplinaNome(horario.getDisciplinaNome());

			Semestre semestre = new Semestre();
			semestre = semestres.findOne(horario.getSemestre().getIdSemestre());
			horarioNovo.setSemestre(semestre);




			Sala sala = new Sala();
			sala = salas.findOne(horario.getSala().getIdSala());

			horarioNovo.setSala(sala);


			HorarioPadrao horarioPadraoAtual = new HorarioPadrao();
			horarioPadraoAtual = horarioPadrao.findOne(horario.getHorarioPadrao().getIdHorarioPadrao());



			horarioNovo.setHorarioPadrao(horarioPadraoAtual);


			horarioNovoSemConflito.add(horarioNovo);
		}

		for(Horario horarioRealocado : horariosConflitantes ) {

			Horario horarioNovo = new Horario();

			horarioNovo.setIdCurso(horarioRealocado.getIdCurso());
			horarioNovo.setDisciplinaId(horarioRealocado.getDisciplinaId());

			horarioNovo.setTurmaPeriodo(horarioRealocado.getTurmaPeriodo());

			horarioNovo.setTipoDescricao(horarioRealocado.getTipoDescricao());
			horarioNovo.setHorarioOrdem(horarioRealocado.getHorarioOrdem());
			horarioNovo.setDiaSemana(horarioRealocado.getDiaSemana());
			horarioNovo.setDisciplinaNome(horarioRealocado.getDisciplinaNome());

			Semestre semestre = new Semestre();
			semestre = semestres.findOne(horarioRealocado.getSemestre().getIdSemestre());
			horarioNovo.setSemestre(semestre);




			Sala sala = new Sala();
			sala = salas.findOne(horarioRealocado.getSala().getIdSala());

			horarioNovo.setSala(sala);


			HorarioPadrao horarioPadraoAtual = new HorarioPadrao();
			horarioPadraoAtual = horarioPadrao.findOne(horarioRealocado.getHorarioPadrao().getIdHorarioPadrao());



			horarioNovo.setHorarioPadrao(horarioPadraoAtual);


			horarioNovoSemConflito.add(horarioNovo);


		}

		horarios.save(horarioNovoSemConflito);

		ModelAndView modelView = new ModelAndView("redirect:/predios");


		return modelView;
	}


	@RequestMapping(value="horarios/checar")
	public String checarAlocacao(Model model, @ModelAttribute("horariosRemove")  List<Horario> horariosRemove, @ModelAttribute("horariosConflitantes") List<Horario> horariosConflitantes, @ModelAttribute("horarioRegistroNovo") List<Horario> horarioRegistroNovo, @RequestParam("disciplinaAlocada") int disciplinaId,
			@RequestParam("salaAlocada") Sala sala, @RequestParam("tipo") int tipo, @RequestParam("ordem") int ordem, @RequestParam("diaSemana") int diaSemana ){
		
		System.out.println("TENTEI CHECAR");
		Horario horarioStatus = horarios.findBySalaAndDiaSemanaAndHorarioOrdemAndTipoDescricaoAndSemestre(sala, diaSemana, ordem, tipo, semestreAtual());


		if(horarioStatus == null) 
		{ 


			int i = 0;

			for(Horario horarioConflitante : horariosConflitantes) {
				if(horarioConflitante.getDisciplinaId() == disciplinaId & horarioConflitante.getDiaSemana() == diaSemana & horarioConflitante.getTipoDescricao() == tipo & horarioConflitante.getHorarioOrdem() == ordem ){

					HashMap<String, String> mensagens = new HashMap<>();
					mensagens.put("classe", "text-success glyphicon glyphicon-ok-sign");
					mensagens.put("status", "Disciplina realocada com sucesso!");

					horariosConflitantes.get(i).setDiaSemana(diaSemana);
					horariosConflitantes.get(i).setHorarioOrdem(ordem);
					horariosConflitantes.get(i).setSala(sala);
					horariosConflitantes.get(i).setTipoDescricao(tipo);
					horariosConflitantes.get(i).setStatus(mensagens);
				}
				i++;	
			}


		}
		else 
		{
			Boolean alocado = false;
			for(Horario horarioConflitante : horariosConflitantes) 
			{
				int i = 0;
				for(Horario horarioRemovido : horariosRemove) { 
					if(horarioRemovido.getDisciplinaId() == horarioStatus.getDisciplinaId() & horarioRemovido.getDiaSemana() == horarioStatus.getDiaSemana() & horarioRemovido.getTipoDescricao() == horarioStatus.getTipoDescricao() & horarioRemovido.getHorarioOrdem() == horarioStatus.getHorarioOrdem() ) {					
						if(horarioConflitante.getDisciplinaId() == disciplinaId & horarioConflitante.getDiaSemana() == diaSemana & horarioConflitante.getTipoDescricao() == tipo & horarioConflitante.getHorarioOrdem() == ordem ){

							HashMap<String, String> mensagens = new HashMap<>();
							mensagens.put("classe", "text-success glyphicon glyphicon-ok-sign");
							mensagens.put("status", "Disciplina realocada com sucesso!");

							horariosConflitantes.get(i).setDiaSemana(diaSemana);
							horariosConflitantes.get(i).setHorarioOrdem(ordem);
							horariosConflitantes.get(i).setSala(sala);
							horariosConflitantes.get(i).setTipoDescricao(tipo);
							horariosConflitantes.get(i).setStatus(mensagens);
							alocado = true;
						}
						i++;	
					}
				}				
				if(!alocado)
				{
					HashMap<String, String> mensagens = new HashMap<>();
					mensagens.put("classe", "text-danger glyphicon glyphicon-remove-sign");
					mensagens.put("status", "Disciplina não alocada!");

					horarioConflitante.setStatus(mensagens);

				}			
			}
		}

		model.addAttribute("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual()));

		return "HorariosConflitantes";
	}


	@RequestMapping(value="horarios/remover", method=RequestMethod.POST, params={"!realocar", "remover"})
	public String remover(Model model, @ModelAttribute("horariosRemove") List<Horario> horariosRemove, @ModelAttribute("horariosConflitantes") List<Horario> horariosConflitantes, @ModelAttribute("horariosCadastrados") List<Horario> horariosCadastrados, @RequestParam("horarioId") int horarioId ){
		
		
		
		System.out.println(horariosConflitantes.size());
		System.out.println(horariosCadastrados.size());
		
		
		Horario horarioRegistrado = horarios.findOne(horarioId);
		for(Horario horario : horariosCadastrados){
			if(horario.getIdHorario() == horarioRegistrado.getIdHorario()) {


				HashMap<String, String> mensagens = new HashMap<>();
				mensagens.put("classe", "text-danger glyphicon glyphicon-remove-sign");
				mensagens.put("status", "Disciplina não alocada!");
				horario.setStatus(mensagens);

				horariosCadastrados.remove(horario);
				horariosConflitantes.add(horario);

				horariosRemove.add(horario);


				break;
			}				
		}
		

		System.out.println("REMOVER UM SALVO:" + horariosRemove.size());
		
		model.addAttribute("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual()));

		return "HorariosConflitantes";
	}

	@RequestMapping(value="horarios/remover", method=RequestMethod.POST, params={"realocar", "!remover"})
	public String realocar(Model model  , @ModelAttribute("horariosConflitantes") List<Horario> horariosConflitantes, @ModelAttribute("horarioRegistroNovo") List<Horario> horarioRegistroNovo, @RequestParam("disciplinaAlocada") int disciplinaId,
			@RequestParam("salaAlocada") Sala sala, @RequestParam("tipo") int tipo, @RequestParam("ordem") int ordem, @RequestParam("diaSemana") int diaSemana){
System.out.println("TENTEI REMOVER!");

		for(Horario horario : horarioRegistroNovo){ 
			if(horario.getDiaSemana() == diaSemana & horario.getHorarioOrdem() == ordem & horario.getDisciplinaId() == disciplinaId & horario.getTipoDescricao() == tipo  ) {


				HashMap<String, String> mensagens = new HashMap<>();
				mensagens.put("classe", "text-danger glyphicon glyphicon-remove-sign");
				mensagens.put("status", "Disciplina não alocada!");

				horario.setStatus(mensagens);

				horarioRegistroNovo.remove(horario);
				horariosConflitantes.add(horario);	
				break;
			}

		}



		model.addAttribute("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual()));

		return "HorariosConflitantes";
	}

	@ModelAttribute("semestreAtual")
	public Semestre semestreAtual() {
		return semestres.findByAtivoIsTrue();
	}

	@ModelAttribute("turnos")
	public Turnos[] turnos() {
		return Turnos.values();
	}

	@ModelAttribute("diasSemana")
	public DiasSemana[] diasSemana(){
		return DiasSemana.values();
	}

	@ModelAttribute("horarioOrdem")
	public HorararioOrdem[] horarioOrdem() {
		return HorararioOrdem.values();
	}

}
