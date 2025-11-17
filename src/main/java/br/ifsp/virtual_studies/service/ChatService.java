package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.chat.ChatRequestDTO;
import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public ChatService(ChatRepository chatRepository, TeacherRepository teacherRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.chatRepository = chatRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public ChatResponseDTO createChat(ChatRequestDTO chatDto) {
        Teacher teacher = teacherRepository.findById(chatDto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        Chat chat = new Chat();
        chat.setSubject(chatDto.getSubject());
        chat.setTeacher(teacher);
        chat.setCreatedAt(LocalDateTime.now());
        Chat createdChat = chatRepository.save(chat);
        return modelMapper.map(createdChat, ChatResponseDTO.class);
    }
    
    public PagedResponse<ChatResponseDTO> getAllChats(Pageable pageable) {
        Page<Chat> chatsPage = chatRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(chatsPage, ChatResponseDTO.class);
    }
    
    public ChatResponseDTO getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        return modelMapper.map(chat, ChatResponseDTO.class);
    }
    
    public ChatResponseDTO updateChat(Long id, ChatRequestDTO chatDto) {
        
        Chat existingChat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + id));
        
        modelMapper.map(chatDto, existingChat);
        existingChat.setId(id);
        Chat updatedChat = chatRepository.save(existingChat);
        return modelMapper.map(updatedChat, ChatResponseDTO.class);
    }
    
    public void deleteChat(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + id));
        
        chatRepository.delete(chat);
    }
}