package com.w2.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.w2.initialize.Inicializacao;
import com.w2.initialize.InicializacaoRepositorio;
import com.w2.model.Centro;
import com.w2.model.Horario;
import com.w2.model.HorarioPadrao;
import com.w2.model.Planta;
import com.w2.model.Predio;
import com.w2.model.Sala;
import com.w2.model.Semestre;
import com.w2.model.support.Ocupacao;
import com.w2.model.util.DiasSemana;
import com.w2.model.util.HorararioOrdem;
import com.w2.model.util.HorarioPadraoContainer;
import com.w2.model.util.HorarioSemestreContainer;
import com.w2.model.util.Mesorregioes;
import com.w2.model.util.Turnos;
import com.w2.repository.CentrosRepositorio;
import com.w2.repository.HorarioRepositorio;
import com.w2.repository.HorariosPadraoRepositorio;
import com.w2.repository.PlantasRepositorio;
import com.w2.repository.PrediosRepositorio;
import com.w2.repository.SalasRepositorio;
import com.w2.repository.SemestreRepositorio;
import com.w2.repository.filter.PredioFiltro;




@Controller
public class CadastroController {

	@Autowired
	private PlantasRepositorio plantas;

	@Autowired
	private Environment env;

	@Autowired
	private HorariosPadraoRepositorio horarioPadrao;

	@Autowired
	private SemestreRepositorio semestres;

	@Autowired
	private CentrosRepositorio centros;

	@Autowired
	private PrediosRepositorio predios;

	@Autowired
	private SalasRepositorio salas;

	@Autowired
	private HorarioRepositorio horarios;


	@Autowired
	private InicializacaoRepositorio inicializadores;


	@ModelAttribute("googleMapsAPIKey")
	public String googleApiKey() {		
		return env.getProperty("maps.api_key");
	}



	@RequestMapping(value="/mapeamento", method=RequestMethod.POST)
	public ModelAndView centro(@RequestParam("idPredio") int idPredio) {

		ModelAndView modelView = new ModelAndView("CadastrarMapeamento");

		Predio predio = predios.findByIdPredio(idPredio);

		modelView.addObject("predio", predio);

		return modelView;
	}


	@RequestMapping(value="/mapeamento/editar", method=RequestMethod.POST)
	public ModelAndView editarMapa(@RequestParam("idplanta") int idPlanta) {

		System.out.println(">>>>>" + idPlanta);


		Planta planta = plantas.findOne(idPlanta);


		Sala salasPlantas = salas.findFirstByPlanta(planta);
		
		System.out.println("Essa é a planta" + salasPlantas);

		List<Horario> horariosCadastrados = horarios.findBySalaAndSemestre(salasPlantas, semestreAtual());


		Ocupacao ocupacao = this.calcOcupacao(salasPlantas, semestreAtual());

		salasPlantas.setOcupacao(ocupacao);

		ModelAndView modelView = new ModelAndView("EditarMapeamento");
		modelView.addObject("planta", planta);
		modelView.addObject("sala", salasPlantas);
		modelView.addObject("horariosCadastrados", horariosCadastrados);
		modelView.addObject("semestreAtual", semestreAtual());
		modelView.addObject("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual()));


		return modelView;



	}



	@RequestMapping(value="/mapeamento/editarnumero", method=RequestMethod.POST)
	public String editarNumeroSala(Model model, @RequestParam("idSala") int idSala, @RequestParam("novoNumero") String novoNumero, @RequestParam("plantaAtual") int idPlanta, @RequestParam("mapeamentoAtualizado") String mapeamento, @RequestParam("predioAtual") Predio predio) {


		Planta planta = plantas.findOne(idPlanta);

		planta.setMapeamento(mapeamento);

		plantas.save(planta);


		Sala salaNumero = salas.findOne(idSala);
		salaNumero.setNumeroSala(novoNumero);

		salas.save(salaNumero);

		List<Horario> horariosCadastrados = horarios.findBySalaAndSemestre(salaNumero, semestreAtual());

		Ocupacao salaOcupacao = this.calcOcupacao(salaNumero, semestreAtual());

		salaNumero.setOcupacao(salaOcupacao);

		model.addAttribute("horariosCadastrados", horariosCadastrados);
		model.addAttribute("sala", salaNumero);

		//Informações estáticas
		model.addAttribute("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual()));

		return "TDHSFragmento";

	}


	@RequestMapping(value="/mapeamento/editar/salvar", method=RequestMethod.POST)
	public String salvarMapeamentoEditado(Planta planta,  Integer[] itemDeletados, RedirectAttributes attributes ) {

		for(Integer item : itemDeletados ) { 
			if(item > 0) {

				System.out.println(item);
				salas.delete(item);
			}
		}


		Predio predio = planta.getPredio();

		String mapa =  planta.getMapeamento() ;
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode arrayNovo = new ArrayNode(null);

		ObjectNode node;




		try {
			node = (ObjectNode) mapper.readValue(mapa, JsonNode.class);


			Iterator<JsonNode> iterator = node.get("objects").iterator();

			while(iterator.hasNext()){

				ObjectNode nodeTemp = (ObjectNode) iterator.next();

				if(nodeTemp.get("idSala").asInt() == 0 ) {

					Sala sala = new Sala();
					Sala salaIdAuxiliar = new Sala();
					sala.setNumeroSala(nodeTemp.get("sala").asText());
					sala.setPredio(predio);
					sala.setPlanta(planta);
					sala.setSemestre(semestreAtual());
					salaIdAuxiliar = salas.save(sala);
					salas.flush();
					nodeTemp.put("idSala", salaIdAuxiliar.getIdSala());				


				}

				arrayNovo.add(nodeTemp);

			}


			node.replace("objects", arrayNovo);

			planta.setMapeamento(node.toString());

			plantas.save(planta);

		} catch (IOException e) {
			e.printStackTrace();
		}


		attributes.addFlashAttribute("mensagem", "Alterações salvas com sucesso!!");
		return "redirect:/predios";
	}


	@RequestMapping(value="/mapeamento/salvar", method=RequestMethod.POST)	
	public String salvarMapeamento(@RequestParam("arquivo") MultipartFile arquivo, Planta planta, @RequestParam("predioid") int predioID, RedirectAttributes attributes ) {   

		Planta plantaSalva = new Planta();

		try {

			String arquivoNome = arquivo.getOriginalFilename();

			String[] arquivoSplit = arquivoNome.split("\\.");
			int extensionIndex = arquivoSplit.length - 1;

			long unixTime = System.currentTimeMillis() / 1000L;

			String arquivoRenomeado = unixTime + "." + arquivoSplit[extensionIndex];

			String diretorio = env.getProperty("indicadores.paths.uploadedFiles");
			if(! new File(diretorio).exists()) { 
				new File(diretorio).mkdir();
			}

			String caminhoArquivo = Paths.get(diretorio, arquivoRenomeado).normalize().toString();

			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(caminhoArquivo)));
			stream.write(arquivo.getBytes());
			stream.close();

			planta.setSemestre(semestreAtual());	
			planta.setArquivoNome(arquivoRenomeado);
			planta.setPredio(predios.findByIdPredio(predioID));

			plantaSalva = plantas.save(planta);
			plantas.flush();




			String mapa =  planta.getMapeamento() ;
			ObjectMapper mapper = new ObjectMapper();

			ArrayNode arrayNovo = new ArrayNode(null);

			ObjectNode  node = (ObjectNode) mapper.readValue(mapa, JsonNode.class);

			Iterator<JsonNode> iterator = node.get("objects").iterator();

			while(iterator.hasNext()){

				ObjectNode nodeTemp = (ObjectNode) iterator.next();

				Sala sala = new Sala();
				Sala salaIdAuxiliar = new Sala();

				sala.setSemestre(semestreAtual());
				sala.setNumeroSala(nodeTemp.get("sala").asText());
				sala.setPredio(predios.findByIdPredio(predioID));
				sala.setPlanta(plantaSalva);
				salaIdAuxiliar = salas.save(sala);
				salas.flush();
				nodeTemp.put("idSala", salaIdAuxiliar.getIdSala());
				nodeTemp.put("fill", "red");
				arrayNovo.add(nodeTemp);

			}
			node.replace("objects", arrayNovo);

			planta.setMapeamento(node.toString());

			plantas.save(planta);		

		} catch (IOException e) {
			e.printStackTrace();
		}



		attributes.addFlashAttribute("mensagem", "Planta salva com sucesso!!");
		return "redirect:/predios";
	}


	@RequestMapping(value="/mapeamento/excluir", method=RequestMethod.POST)
	public ModelAndView excluirPlanta(@RequestParam("plantaRemover") Planta planta, RedirectAttributes attributes) {


		List<Sala> salasRemover = salas.findByPlanta(planta);
		List<Horario> horariosRemover = horarios.findBySemestreAndSalaIn(semestreAtual(), salasRemover);

		try { 


			String diretorio = env.getProperty("indicadores.paths.uploadedFiles");
			String caminhoArquivo = Paths.get(diretorio, planta.getArquivoNome()).normalize().toString();

			File file = new File(caminhoArquivo);
			file.delete();

			horarios.delete(horariosRemover);

			salas.delete(salasRemover);

			plantas.delete(planta);





		} catch (Exception e) {

			attributes.addFlashAttribute("mensagem", "Ocorreu um erro");
			return new ModelAndView("redirect:/predios");	
		}	


		attributes.addFlashAttribute("mensagem", "Planta excluída com sucesso!!");
		return new ModelAndView("redirect:/predios");
	}



	@RequestMapping(value="/semestres")
	public ModelAndView configurarSemestres() {


		ModelAndView modelView = new ModelAndView("StatusSemestre");

		List<Semestre> todosSemestres = semestres.findAllByOrderByIdSemestreDesc();

		modelView.addObject("cadSemestres", todosSemestres);
		return modelView;
	}


	@RequestMapping(value="/semestres", method=RequestMethod.POST)
	public String editarSemestres(HorarioSemestreContainer horarioSemestre) {



		semestres.save(horarioSemestre.getSemestre());
		horarioPadrao.save(horarioSemestre.getHorariosPadrao());


		return "redirect:/semestres";
	}



	@RequestMapping(value="/semestre/editar")
	public ModelAndView editarSemestres(@RequestParam("idEditar") int idSemestre) {


		Semestre semestre = semestres.findOne(idSemestre);

		List<HorarioPadrao> horariosPadrao = horarioPadrao.findBySemestre(semestre);

		HorarioSemestreContainer horarioSemestre = new HorarioSemestreContainer();

		horarioSemestre.setSemestre(semestre);
		horarioSemestre.setHorariosPadrao(horariosPadrao);


		ModelAndView modelView = new ModelAndView("EditarSemestre");

		modelView.addObject("horarioSemestre", horarioSemestre);

		return modelView;
	}



	@RequestMapping(value="/cadsemestre")
	public ModelAndView cadadastrarSemestre() { 
		
		ModelAndView modelView = new ModelAndView("CadastrarSemestre");
		
		List<Predio> prediosTotal = predios.findBySemestre(semestreAtual());
		List<Sala> salasTotal = salas.findBySemestre(semestreAtual());
		
		modelView.addObject("salaTotal", salasTotal.size());
		modelView.addObject("predioTotal", prediosTotal.size());
		
		
		
		return modelView;
	}


	@RequestMapping(value="/cadsemestre", method=RequestMethod.POST)
	public String salvarSemestre(@RequestParam("semestre.ano") int ano,@RequestParam("semestre.identificador") int identificador, HorarioPadraoContainer horarios, int replicarDados, RedirectAttributes attributes) { 


		System.out.println(replicarDados);
		
		
		Semestre semestreAtivo = semestres.findByAtivoIsTrue();
		
 
		if(semestreAtivo != null) {
			System.out.println("Vou mudar o contexto!");

			semestreAtivo.setAtivo(false);
			semestres.save(semestreAtivo);

		}


		Semestre semestre = new Semestre();
		semestre.setAno(ano);
		semestre.setSemestre(identificador);
		semestre.setAtivo(true);
		Semestre semestreSalvo = this.semestres.save(semestre);
		this.semestres.flush();

		ArrayList<HorarioPadrao> listaHorarios = horarios.getHorarios(); 
		for(HorarioPadrao horarioPadrao : listaHorarios) {
			horarioPadrao.setSemestre(semestreSalvo);
			this.horarioPadrao.save(horarioPadrao);
		}

	


		List<Inicializacao> inicializar = inicializadores.findAll();

		if(!inicializar.get(0).getStatus()){ 

			inicializar.get(0).setStatus(true);
			inicializadores.save(inicializar.get(0));
			
			return "redirect:/finalizarconfiguracao";			
		}

		
		if(replicarDados == 1) { 
			
			List<Predio> prediosSalvos = predios.findBySemestre(semestreAtivo);
			for(Predio predio : prediosSalvos) { 
				
				List<Planta> plantasSalvas = plantas.findByPredio(predio);
				Predio predioAuxiliar = new Predio();
			
				
				predios.detachObject(predio);
				predio.setIdPredio(0);
				predio.setSemestre(semestreSalvo);
				predioAuxiliar = predios.save(predio);
				predios.flush();
							
			
				System.out.println("ACHEI PLANTAS ::::" + plantasSalvas.size());
				for(Planta planta : plantasSalvas) {
										
					Planta plantaAuxiliar = new Planta();

					plantas.detachObject(planta);
					planta.setIdPlanta(0);
					planta.setPredio(predioAuxiliar);
					planta.setSemestre(semestreSalvo);
					
					plantaAuxiliar = plantas.save(planta);
					plantas.flush();
					
					
					
					String mapa =  planta.getMapeamento() ;
					ObjectMapper mapper = new ObjectMapper();

					ArrayNode arrayNovo = new ArrayNode(null);

					ObjectNode node;
					try {
						node = (ObjectNode) mapper.readValue(mapa, JsonNode.class);
				

					Iterator<JsonNode> iterator = node.get("objects").iterator();

					while(iterator.hasNext()){

						ObjectNode nodeTemp = (ObjectNode) iterator.next();

						Sala sala = new Sala();
						Sala salaIdAuxiliar = new Sala();

						sala.setSemestre(semestreAtual());
						sala.setNumeroSala(nodeTemp.get("sala").asText());
						sala.setPredio(predioAuxiliar);
						sala.setPlanta(plantaAuxiliar);
						salaIdAuxiliar = salas.save(sala);
						salas.flush();
						nodeTemp.put("idSala", salaIdAuxiliar.getIdSala());
						nodeTemp.put("fill", "red");
						arrayNovo.add(nodeTemp);

					}
					node.replace("objects", arrayNovo);

					planta.setMapeamento(node.toString());

					plantas.save(planta);							
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}		
			}
			
			
		}

		return "redirect:/semestres";
	}


	@RequestMapping(value="/centros", method=RequestMethod.POST)
	public String salvarCentro(Centro centro) {		
		centros.save(centro);
		return "redirect:/centros";
	}


	@RequestMapping(value="/centros")
	public ModelAndView configurarCentros() {	
		List<Centro> todosCentros = centros.findAll();
		ModelAndView modelView = new ModelAndView("StatusCentro");
		modelView.addObject("centros", todosCentros);
		return modelView;
	}



	@RequestMapping("centro/editar/{codigo}")
	public ModelAndView cadastrarCentro(@PathVariable int codigo) {
		Centro centro = centros.findCentroByIdCentro(codigo);

		ModelAndView modelView = new ModelAndView("CadastrarCentro");
		modelView.addObject("centro", centro);
		modelView.addObject("mesorregioes", Mesorregioes.values());		
		return modelView;
	}


	@RequestMapping(value="/predios")
	public ModelAndView configurarPredios(@ModelAttribute("filtro") PredioFiltro filtro ) { 

		String sigla;
		if(filtro.getSigla() == null || filtro.getSigla().equals(""))
		{ 
			sigla =	"%"; 
		}
		else 
		{ 
			sigla = filtro.getSigla();
		}



		List<Centro> todosCentros = centros.findAll();

		List<Predio> todosPredios = predios.findPredioBySemestreAndCentro_SiglaLike(semestreAtual(), sigla);

		List<Planta> todasPlantas = plantas.findAll();



		ModelAndView modelView = new ModelAndView("StatusPredio");
		modelView.addObject("predios", todosPredios);
		modelView.addObject("centros", todosCentros);
		modelView.addObject("plantas", todasPlantas);
		return modelView;
	}



	@RequestMapping(value="/cadpredio/{codigo}")
	public ModelAndView cadastrarPredio(@PathVariable int codigo) {
		Predio predio = new Predio();
		Centro centro = centros.findCentroByIdCentro(codigo);
		predio.setCentro(centro);
		ModelAndView modelView = new ModelAndView("CadastrarPredio");
		modelView.addObject("predio", predio);

		return modelView;
	}

	@RequestMapping(value="/cadpredio/editar/{codigo}") 
	public ModelAndView editarPredio(@PathVariable int codigo) { 
		Predio predioEditar = predios.findOne(codigo);
		ModelAndView modelView = new ModelAndView("CadastrarPredio");
		modelView.addObject("predio", predioEditar);
		return modelView;
	}


	@RequestMapping(value="/cadpredio/{codigo}", method=RequestMethod.POST)
	public ModelAndView salvarPredio(Predio predio, RedirectAttributes attributes) { 

		Centro centro = centros.findCentroByIdCentro(predio.getCentro().getIdCentro());
		predio.setCentro(centro);
		predio.setSemestre(semestreAtual());
		try {

			predios.save(predio);
		}
		catch (Exception e) {
			ModelAndView mv = new ModelAndView("CadastrarPredio");
			mv.addObject("mensagem", "OCORREU UM ERRO: " + e);
			return mv;	
		}


		attributes.addFlashAttribute("mensagem", "Prédio salvo com sucesso!!");
		return new ModelAndView("redirect:/predios");
	}



	@RequestMapping(value="/predio/excluir", method = RequestMethod.POST) 
	public ModelAndView excluirPredio(@RequestParam("predioRemover") Predio predio, RedirectAttributes attributes) {


		List<Sala> salasRemover = salas.findByPredio(predio);
		List<Horario> horariosRemover = horarios.findBySemestreAndSalaIn(semestreAtual(), salasRemover);
		List<Planta> plantasRemover = plantas.findByPredio(predio);



		try { 


			horarios.delete(horariosRemover);

			salas.delete(salasRemover);

			plantas.delete(plantasRemover);

			predios.delete(predio);

		} catch (Exception e) {

			attributes.addFlashAttribute("mensagem", "Ocorreu um erro");
			return new ModelAndView("redirect:/predios");	
		}	


		attributes.addFlashAttribute("mensagem", "Prédio excluído com sucesso!!");
		return new ModelAndView("redirect:/predios");
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



	@ModelAttribute("semestreAtual")
	public Semestre semestreAtual() {
		return semestres.findByAtivoIsTrue();
	}


	public Ocupacao calcOcupacao(Sala sala, Semestre semestre) {

		Ocupacao salaOcupacao = new Ocupacao();

		int  total = horarios.findBySalaAndSemestre(sala, semestre).size();
		int matutino = horarios.findBySalaAndTipoDescricao(sala, Turnos.MANHA.getTipo()).size();
		int vespertino = horarios.findBySalaAndTipoDescricao(sala, Turnos.TARDE.getTipo()).size();
		int noturno = horarios.findBySalaAndTipoDescricao(sala, Turnos.NOITE.getTipo()).size();

		salaOcupacao.setTotal(total);
		salaOcupacao.setMatutino(matutino);
		salaOcupacao.setVespertino(vespertino);
		salaOcupacao.setNoturno(noturno);


		return salaOcupacao;
	}


}



